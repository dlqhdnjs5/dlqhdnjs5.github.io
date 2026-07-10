(() => {
  const THREE = window.THREE;
  const canvas = document.querySelector("#system-canvas");
  const progressFill = document.querySelector("#progress-fill");
  const progressIndex = document.querySelector("#progress-index");
  const progressLabel = document.querySelector("#progress-label");
  const incidentButton = document.querySelector("#incident-button");
  const incidentStatus = document.querySelector("#incident-status");
  const idcAElement = document.querySelector("#idc-a");
  const idcAStrong = idcAElement?.querySelector("strong");
  const protocolSteps = [...document.querySelectorAll(".protocol-steps li")];
  const sceneSections = [...document.querySelectorAll("[data-scene]")];
  const reducedMotion = window.matchMedia("(prefers-reduced-motion: reduce)").matches;

  if (!THREE || !canvas) return;

  const scene = new THREE.Scene();
  scene.fog = new THREE.FogExp2(0x030506, 0.038);

  const camera = new THREE.PerspectiveCamera(44, window.innerWidth / window.innerHeight, 0.1, 140);
  camera.position.set(0, 2.2, 16);

  let renderer;
  try {
    renderer = new THREE.WebGLRenderer({
      canvas,
      antialias: true,
      alpha: true,
      powerPreference: "high-performance",
      preserveDrawingBuffer: true,
    });
  } catch (error) {
    document.body.classList.add("webgl-unavailable");
    return;
  }

  renderer.setPixelRatio(Math.min(window.devicePixelRatio || 1, 1.8));
  renderer.setSize(window.innerWidth, window.innerHeight);
  renderer.setClearColor(0x030506, 1);
  renderer.outputColorSpace = THREE.SRGBColorSpace;
  renderer.toneMapping = THREE.ACESFilmicToneMapping;
  renderer.toneMappingExposure = 1.15;

  const palette = {
    cyan: 0x41e8f5,
    coral: 0xff705d,
    mint: 0x63e6a7,
    amber: 0xf6c963,
    blue: 0x6c8cff,
    white: 0xf4f7f8,
    dark: 0x071012,
  };

  const world = new THREE.Group();
  const coreGroup = new THREE.Group();
  const nodeGroup = new THREE.Group();
  const streamGroup = new THREE.Group();
  const filterGroup = new THREE.Group();
  const idcGroup = new THREE.Group();
  world.add(streamGroup, coreGroup, nodeGroup, filterGroup, idcGroup);
  scene.add(world);

  const ambient = new THREE.AmbientLight(0x8ea0aa, 0.84);
  const keyLight = new THREE.DirectionalLight(0xe9f8ff, 2.4);
  keyLight.position.set(4, 8, 10);
  const cyanLight = new THREE.PointLight(palette.cyan, 24, 24, 2);
  cyanLight.position.set(-4, 2, 5);
  const coralLight = new THREE.PointLight(palette.coral, 18, 20, 2);
  coralLight.position.set(6, -2, 4);
  scene.add(ambient, keyLight, cyanLight, coralLight);

  const grid = new THREE.GridHelper(48, 48, 0x30444a, 0x162329);
  grid.position.y = -5.1;
  grid.material.transparent = true;
  grid.material.opacity = 0.2;
  scene.add(grid);

  function clamp(value, min = 0, max = 1) {
    return Math.min(Math.max(value, min), max);
  }

  function lerp(start, end, amount) {
    return start + (end - start) * amount;
  }

  function smoothstep(edge0, edge1, value) {
    const x = clamp((value - edge0) / Math.max(edge1 - edge0, 0.0001));
    return x * x * (3 - 2 * x);
  }

  function registerMaterial(material, baseOpacity = 1) {
    material.transparent = true;
    material.opacity = baseOpacity;
    material.userData.baseOpacity = baseOpacity;
    return material;
  }

  function standardMaterial(color, baseOpacity = 1, emissiveStrength = 0.5) {
    return registerMaterial(
      new THREE.MeshStandardMaterial({
        color,
        emissive: color,
        emissiveIntensity: emissiveStrength,
        metalness: 0.74,
        roughness: 0.28,
        depthWrite: baseOpacity >= 0.95,
      }),
      baseOpacity,
    );
  }

  function basicMaterial(color, baseOpacity = 1) {
    return registerMaterial(
      new THREE.MeshBasicMaterial({
        color,
        blending: THREE.AdditiveBlending,
        depthWrite: false,
      }),
      baseOpacity,
    );
  }

  function setGroupOpacity(group, alpha) {
    group.traverse((object) => {
      const materials = Array.isArray(object.material) ? object.material : [object.material];
      materials.filter(Boolean).forEach((material) => {
        const baseOpacity = material.userData.baseOpacity ?? 1;
        material.transparent = true;
        material.opacity = baseOpacity * alpha;
        material.visible = material.opacity > 0.002;
      });
    });
  }

  function createLabelSprite(text, accent = "#41e8f5") {
    const labelCanvas = document.createElement("canvas");
    labelCanvas.width = 640;
    labelCanvas.height = 180;
    const context = labelCanvas.getContext("2d");
    context.clearRect(0, 0, labelCanvas.width, labelCanvas.height);
    context.fillStyle = "rgba(4, 8, 10, 0.9)";
    context.fillRect(8, 8, 624, 164);
    context.strokeStyle = accent;
    context.lineWidth = 5;
    context.strokeRect(8, 8, 624, 164);
    context.fillStyle = accent;
    context.fillRect(32, 38, 10, 104);
    context.fillStyle = "#f4f7f8";
    context.font = "900 48px Inter, Arial, sans-serif";
    context.textBaseline = "middle";
    context.fillText(text, 72, 92, 520);

    const texture = new THREE.CanvasTexture(labelCanvas);
    texture.colorSpace = THREE.SRGBColorSpace;
    const material = registerMaterial(new THREE.SpriteMaterial({ map: texture, depthWrite: false }), 0.94);
    const sprite = new THREE.Sprite(material);
    sprite.scale.set(3.2, 0.9, 1);
    return sprite;
  }

  function makeCore() {
    const shellMaterial = standardMaterial(palette.dark, 0.9, 0.18);
    const shell = new THREE.Mesh(new THREE.CylinderGeometry(2.12, 2.12, 0.34, 6, 1, false), shellMaterial);
    shell.rotation.x = Math.PI / 2;
    coreGroup.add(shell);

    const shellEdges = new THREE.LineSegments(
      new THREE.EdgesGeometry(shell.geometry),
      registerMaterial(new THREE.LineBasicMaterial({ color: palette.cyan }), 0.8),
    );
    shellEdges.rotation.copy(shell.rotation);
    coreGroup.add(shellEdges);

    const core = new THREE.Mesh(
      new THREE.OctahedronGeometry(1.02, 1),
      standardMaterial(palette.cyan, 0.72, 1.25),
    );
    core.userData.kind = "core";
    coreGroup.add(core);

    const coreWire = new THREE.LineSegments(
      new THREE.EdgesGeometry(new THREE.OctahedronGeometry(1.38, 1)),
      registerMaterial(new THREE.LineBasicMaterial({ color: palette.white }), 0.36),
    );
    coreWire.userData.kind = "coreWire";
    coreGroup.add(coreWire);

    [2.72, 3.18, 3.62].forEach((radius, index) => {
      const ring = new THREE.Mesh(
        new THREE.TorusGeometry(radius, index === 1 ? 0.045 : 0.025, 10, 100),
        basicMaterial([palette.cyan, palette.amber, palette.coral][index], 0.48),
      );
      ring.rotation.x = 0.24 + index * 0.36;
      ring.rotation.y = -0.28 + index * 0.22;
      ring.userData.kind = "ring";
      ring.userData.speed = index % 2 === 0 ? 1 : -1;
      coreGroup.add(ring);
    });

    for (let i = 0; i < 12; i += 1) {
      const angle = (i / 12) * Math.PI * 2;
      const module = new THREE.Mesh(
        new THREE.BoxGeometry(0.24, 0.54, 0.16),
        standardMaterial(i % 3 === 0 ? palette.amber : palette.cyan, 0.82, 0.8),
      );
      module.position.set(Math.cos(angle) * 4.15, Math.sin(angle) * 4.15, Math.sin(angle * 2) * 0.35);
      module.rotation.z = angle + Math.PI / 2;
      module.userData.kind = "module";
      coreGroup.add(module);
    }

    const label = createLabelSprite("PROCESSING CORE");
    label.position.set(0, -2.55, 0.7);
    label.scale.set(3.8, 1.05, 1);
    coreGroup.add(label);
  }

  const nodeDefinitions = [
    { label: "API GATEWAY", position: [-6.1, 2.25, -0.6], color: palette.blue, accent: "#6c8cff" },
    { label: "KAFKA", position: [-4.6, -1.85, 0.2], color: palette.amber, accent: "#f6c963" },
    { label: "RULE ENGINE", position: [-0.8, 3.2, -0.4], color: palette.cyan, accent: "#41e8f5" },
    { label: "REDIS SENTINEL", position: [4.3, -2.15, 0.15], color: palette.coral, accent: "#ff705d" },
    { label: "MYSQL", position: [6.15, 1.8, -0.7], color: palette.mint, accent: "#63e6a7" },
    { label: "AI AGENT", position: [3.7, 3.95, -1.1], color: palette.blue, accent: "#6c8cff" },
  ];

  const connectionMaterials = [];
  const nodeMeshes = [];

  function makeSystemNode(definition, index) {
    const group = new THREE.Group();
    group.position.set(...definition.position);
    group.userData.basePosition = new THREE.Vector3(...definition.position);
    group.userData.floatOffset = index * 0.86;

    const mesh = new THREE.Mesh(
      new THREE.BoxGeometry(2.65, 1.32, 0.72),
      standardMaterial(0x0b1115, 0.92, 0.16),
    );
    group.add(mesh);

    const edge = new THREE.LineSegments(
      new THREE.EdgesGeometry(mesh.geometry),
      registerMaterial(new THREE.LineBasicMaterial({ color: definition.color }), 0.82),
    );
    group.add(edge);

    const portMaterial = standardMaterial(definition.color, 0.95, 1.1);
    for (let i = -1; i <= 1; i += 1) {
      const port = new THREE.Mesh(new THREE.BoxGeometry(0.38, 0.1, 0.08), portMaterial);
      port.position.set(i * 0.62, -0.36, 0.4);
      group.add(port);
    }

    const label = createLabelSprite(definition.label, definition.accent);
    label.position.set(0, 0.08, 0.48);
    label.scale.set(2.45, 0.69, 1);
    group.add(label);
    nodeMeshes.push(group);
    nodeGroup.add(group);

    const direction = group.position.clone().normalize();
    const mid = group.position.clone().multiplyScalar(0.5);
    mid.z += index % 2 === 0 ? 1.7 : -1.7;
    const curve = new THREE.QuadraticBezierCurve3(direction.multiplyScalar(2.2), mid, group.position.clone());
    const geometry = new THREE.TubeGeometry(curve, 44, 0.018, 6, false);
    const material = basicMaterial(definition.color, 0.42);
    connectionMaterials.push(material);
    nodeGroup.add(new THREE.Mesh(geometry, material));
  }

  const streamCurves = [];
  const streamLineMaterials = [];
  let particleGeometry;
  let particlePositionAttribute;
  const particleMetadata = [];

  function makeDataStream() {
    const curveTotal = 14;
    const streamColors = [palette.cyan, palette.amber, palette.mint, palette.coral, palette.blue];

    for (let i = 0; i < curveTotal; i += 1) {
      const normalized = i / (curveTotal - 1) - 0.5;
      const phase = i * 0.47;
      const points = [
        new THREE.Vector3(-15, normalized * 9.5, Math.sin(phase) * 2.6),
        new THREE.Vector3(-8, normalized * 5.6 + Math.sin(phase) * 0.8, Math.cos(phase) * 1.8),
        new THREE.Vector3(-3.5, normalized * 2.2, Math.sin(phase * 1.4) * 0.9),
        new THREE.Vector3(0, normalized * 0.38, Math.cos(phase) * 0.45),
        new THREE.Vector3(3.8, normalized * 1.7, Math.sin(phase) * 0.9),
        new THREE.Vector3(8.5, normalized * 5.2 + Math.cos(phase) * 0.9, Math.cos(phase * 1.2) * 1.7),
        new THREE.Vector3(15, normalized * 10.5, Math.sin(phase * 1.6) * 2.5),
      ];
      const curve = new THREE.CatmullRomCurve3(points);
      streamCurves.push(curve);

      if (i % 2 === 0) {
        const geometry = new THREE.TubeGeometry(curve, 130, i % 4 === 0 ? 0.018 : 0.011, 6, false);
        const material = basicMaterial(streamColors[i % streamColors.length], i % 4 === 0 ? 0.36 : 0.22);
        streamLineMaterials.push(material);
        streamGroup.add(new THREE.Mesh(geometry, material));
      }
    }

    const particleCount = window.innerWidth < 720 ? 300 : 620;
    const positions = new Float32Array(particleCount * 3);
    const colors = new Float32Array(particleCount * 3);

    for (let i = 0; i < particleCount; i += 1) {
      const curveIndex = i % curveTotal;
      const color = new THREE.Color(streamColors[curveIndex % streamColors.length]);
      particleMetadata.push({
        curveIndex,
        offset: Math.random(),
        speed: 0.018 + Math.random() * 0.032,
        wave: Math.random() * Math.PI * 2,
      });
      colors[i * 3] = color.r;
      colors[i * 3 + 1] = color.g;
      colors[i * 3 + 2] = color.b;
    }

    particleGeometry = new THREE.BufferGeometry();
    particlePositionAttribute = new THREE.BufferAttribute(positions, 3);
    particleGeometry.setAttribute("position", particlePositionAttribute);
    particleGeometry.setAttribute("color", new THREE.BufferAttribute(colors, 3));
    const particleMaterial = registerMaterial(
      new THREE.PointsMaterial({
        size: window.innerWidth < 720 ? 0.075 : 0.09,
        vertexColors: true,
        blending: THREE.AdditiveBlending,
        sizeAttenuation: true,
        depthWrite: false,
      }),
      0.94,
    );
    const particles = new THREE.Points(particleGeometry, particleMaterial);
    particles.userData.kind = "particles";
    streamGroup.add(particles);
  }

  const filterRings = [];

  function makeFilterGate() {
    const colors = [palette.coral, palette.amber, palette.mint];
    const labels = ["FILTER", "INDEX", "STORE"];

    [-4.2, 0, 4.2].forEach((x, index) => {
      const group = new THREE.Group();
      group.position.x = x;
      const ring = new THREE.Mesh(
        new THREE.TorusGeometry(2.4 - index * 0.22, 0.07, 8, 6),
        standardMaterial(colors[index], 0.72, 1.15),
      );
      ring.rotation.y = Math.PI / 2;
      ring.rotation.z = Math.PI / 6;
      group.add(ring);

      const innerRing = new THREE.Mesh(
        new THREE.TorusGeometry(1.72 - index * 0.18, 0.018, 6, 6),
        basicMaterial(colors[index], 0.62),
      );
      innerRing.rotation.copy(ring.rotation);
      group.add(innerRing);

      const label = createLabelSprite(labels[index], `#${colors[index].toString(16).padStart(6, "0")}`);
      label.position.set(0, -3.05, 0);
      label.scale.set(2.5, 0.7, 1);
      group.add(label);
      group.userData.ring = ring;
      group.userData.innerRing = innerRing;
      filterRings.push(group);
      filterGroup.add(group);
    });
    filterGroup.rotation.y = -0.12;
  }

  const idcMeshes = [];
  const trafficDots = [];
  let idcConnection;

  function makeServerRack(label, color) {
    const group = new THREE.Group();
    const frame = new THREE.Mesh(
      new THREE.BoxGeometry(3.35, 4.45, 1.55),
      standardMaterial(0x0a0f13, 0.94, 0.1),
    );
    group.add(frame);
    const frameEdge = new THREE.LineSegments(
      new THREE.EdgesGeometry(frame.geometry),
      registerMaterial(new THREE.LineBasicMaterial({ color }), 0.8),
    );
    group.add(frameEdge);

    for (let i = 0; i < 6; i += 1) {
      const module = new THREE.Mesh(
        new THREE.BoxGeometry(2.75, 0.42, 1.62),
        standardMaterial(i % 2 === 0 ? color : 0x152027, 0.88, i % 2 === 0 ? 0.72 : 0.15),
      );
      module.position.y = 1.55 - i * 0.62;
      group.add(module);

      const port = new THREE.Mesh(new THREE.BoxGeometry(0.65, 0.06, 0.04), basicMaterial(color, 0.95));
      port.position.set(0.78, module.position.y, 0.82);
      group.add(port);
    }

    const title = createLabelSprite(label, `#${color.toString(16).padStart(6, "0")}`);
    title.position.set(0, -2.85, 0.6);
    title.scale.set(2.8, 0.78, 1);
    group.add(title);
    idcMeshes.push(group);
    return group;
  }

  function makeIdcScene() {
    const idcA = makeServerRack("IDC A / ACTIVE", palette.cyan);
    const idcB = makeServerRack("IDC B / ACTIVE", palette.mint);
    idcA.position.x = -4.7;
    idcB.position.x = 4.7;
    idcGroup.add(idcA, idcB);

    const connectionGeometry = new THREE.BufferGeometry().setFromPoints([
      new THREE.Vector3(-3, 0, 0.4),
      new THREE.Vector3(3, 0, 0.4),
    ]);
    idcConnection = new THREE.Line(
      connectionGeometry,
      registerMaterial(new THREE.LineBasicMaterial({ color: palette.mint }), 0.78),
    );
    idcGroup.add(idcConnection);

    for (let i = 0; i < 14; i += 1) {
      const dot = new THREE.Mesh(new THREE.BoxGeometry(0.12, 0.12, 0.12), basicMaterial(palette.mint, 0.9));
      dot.userData.offset = i / 14;
      trafficDots.push(dot);
      idcGroup.add(dot);
    }

    idcGroup.position.y = 0.3;
  }

  makeCore();
  nodeDefinitions.forEach(makeSystemNode);
  makeDataStream();
  makeFilterGate();
  makeIdcScene();
  setGroupOpacity(nodeGroup, 0);
  setGroupOpacity(filterGroup, 0);
  setGroupOpacity(idcGroup, 0);

  let scrollProgress = 0;
  let storyProgress = 0;
  let activeScene = "intro";
  let pointerX = 0;
  let pointerY = 0;
  let incidentPhase = 0;
  let targetIncidentPhase = 0;
  let incidentRunning = false;
  let incidentTimers = [];

  const sceneMetadata = {
    intro: ["01", "INTRO"],
    skills: ["02", "SKILL"],
    achievements: ["03", "ACHIEVEMENTS"],
    leadership: ["04", "LEADERSHIP"],
    career: ["05", "CAREER"],
    projects: ["06", "PROJECTS"],
  };

  function updateScrollState() {
    const maxScroll = Math.max(document.documentElement.scrollHeight - window.innerHeight, 1);
    scrollProgress = clamp(window.scrollY / maxScroll);
    if (progressFill) progressFill.style.height = `${scrollProgress * 100}%`;

    const leadershipSection = document.querySelector("#leadership");
    const storyEnd = leadershipSection
      ? leadershipSection.offsetTop + leadershipSection.offsetHeight - window.innerHeight
      : maxScroll * 0.64;
    storyProgress = clamp(window.scrollY / Math.max(storyEnd, 1));

    let nextScene = activeScene;
    sceneSections.forEach((section) => {
      const rect = section.getBoundingClientRect();
      if (rect.top <= window.innerHeight * 0.54 && rect.bottom > window.innerHeight * 0.32) {
        nextScene = section.dataset.scene;
      }
    });

    if (nextScene !== activeScene) {
      activeScene = nextScene;
      document.body.dataset.scene = activeScene;
      const [index, label] = sceneMetadata[activeScene] || ["00", "SYSTEM"];
      if (progressIndex) progressIndex.textContent = index;
      if (progressLabel) progressLabel.textContent = label;
    }
  }

  function updateParticles(elapsed) {
    if (!particlePositionAttribute) return;
    const positions = particlePositionAttribute.array;
    const motionTime = reducedMotion ? 0.35 : elapsed;

    particleMetadata.forEach((metadata, index) => {
      const t = (metadata.offset + motionTime * metadata.speed) % 1;
      const point = streamCurves[metadata.curveIndex].getPointAt(t);
      const energy = Math.sin(t * Math.PI);
      positions[index * 3] = point.x;
      positions[index * 3 + 1] = point.y + Math.sin(motionTime * 1.8 + metadata.wave) * 0.045 * energy;
      positions[index * 3 + 2] = point.z + Math.cos(motionTime * 1.45 + metadata.wave) * 0.065 * energy;
    });
    particlePositionAttribute.needsUpdate = true;
  }

  function interpolateCamera(progress) {
    const keyframes = [
      { at: 0, position: [0, 2.2, 16], target: [0, 0, 0] },
      { at: 0.22, position: [1.2, 1.5, 13.2], target: [0, 0.1, 0] },
      { at: 0.44, position: [-2.8, 3.6, 14], target: [0.7, 0, 0] },
      { at: 0.66, position: [2.2, 2.6, 15.4], target: [0.8, -0.1, 0] },
      { at: 0.84, position: [0, 3.2, 14.4], target: [0, 0, 0] },
      { at: 1, position: [0, 1.4, 12.8], target: [0, 0, 0] },
    ];

    let start = keyframes[0];
    let end = keyframes[keyframes.length - 1];
    for (let i = 0; i < keyframes.length - 1; i += 1) {
      if (progress >= keyframes[i].at && progress <= keyframes[i + 1].at) {
        start = keyframes[i];
        end = keyframes[i + 1];
        break;
      }
    }

    const local = smoothstep(start.at, end.at, progress);
    const px = lerp(start.position[0], end.position[0], local) + pointerX * 0.42;
    const py = lerp(start.position[1], end.position[1], local) - pointerY * 0.28;
    const pz = lerp(start.position[2], end.position[2], local);
    camera.position.lerp(new THREE.Vector3(px, py, pz), 0.055);

    const target = new THREE.Vector3(
      lerp(start.target[0], end.target[0], local) + pointerX * 0.15,
      lerp(start.target[1], end.target[1], local) - pointerY * 0.1,
      lerp(start.target[2], end.target[2], local),
    );
    camera.lookAt(target);
  }

  function updateScene(elapsed) {
    const skillReveal = smoothstep(0.12, 0.34, storyProgress) * (1 - smoothstep(0.48, 0.63, storyProgress));
    const filterReveal = smoothstep(0.43, 0.58, storyProgress) * (1 - smoothstep(0.7, 0.8, storyProgress));
    const idcReveal = smoothstep(0.66, 0.82, storyProgress);
    const coreFade = 1 - smoothstep(0.58, 0.82, storyProgress);
    const motionTime = reducedMotion ? 0.5 : elapsed;

    coreGroup.position.x = lerp(0, -5.8, filterReveal);
    coreGroup.position.y = lerp(0, -3.8, filterReveal);
    coreGroup.scale.setScalar(1 - filterReveal * 0.28);
    setGroupOpacity(coreGroup, Math.max(coreFade, 0.06));

    coreGroup.children.forEach((child, index) => {
      if (child.userData.kind === "core") {
        child.rotation.x = motionTime * 0.34 + storyProgress * 1.4;
        child.rotation.y = motionTime * 0.48;
      }
      if (child.userData.kind === "coreWire") {
        child.rotation.x = -motionTime * 0.22;
        child.rotation.y = motionTime * 0.31;
      }
      if (child.userData.kind === "ring") {
        child.rotation.z = motionTime * 0.045 * child.userData.speed + storyProgress * (index + 1) * 0.04;
      }
      if (child.userData.kind === "module") {
        child.scale.y = 0.8 + Math.sin(motionTime * 2.2 + index) * 0.18;
      }
    });

    nodeMeshes.forEach((node, index) => {
      const base = node.userData.basePosition;
      node.position.x = base.x * (0.58 + skillReveal * 0.42);
      node.position.y = base.y * (0.58 + skillReveal * 0.42) + Math.sin(motionTime * 0.62 + node.userData.floatOffset) * 0.14;
      node.position.z = base.z;
      node.rotation.y = Math.sin(motionTime * 0.34 + index) * 0.055;
    });
    setGroupOpacity(nodeGroup, skillReveal);

    filterGroup.position.x = 0;
    filterGroup.position.y = lerp(4.2, 0.4, filterReveal);
    filterGroup.scale.setScalar(0.72 + filterReveal * 0.28);
    filterRings.forEach((group, index) => {
      group.rotation.x = Math.sin(motionTime * 0.42 + index) * 0.08;
      group.userData.ring.rotation.x = motionTime * 0.17 * (index % 2 === 0 ? 1 : -1);
      group.userData.innerRing.rotation.x = -motionTime * 0.12;
    });
    setGroupOpacity(filterGroup, filterReveal);

    idcGroup.position.y = lerp(4.8, 0.25, idcReveal);
    idcGroup.scale.setScalar(0.76 + idcReveal * 0.24);
    setGroupOpacity(idcGroup, idcReveal);

    incidentPhase = lerp(incidentPhase, targetIncidentPhase, 0.06);
    const idcA = idcMeshes[0];
    const idcB = idcMeshes[1];
    if (idcA && idcB) {
      idcA.rotation.z = Math.sin(motionTime * 7) * 0.012 * incidentPhase;
      idcA.scale.setScalar(1 - incidentPhase * 0.08);
      idcB.scale.setScalar(1 + incidentPhase * 0.04);
      idcA.traverse((object) => {
        if (object.material?.emissive) {
          object.material.emissive.lerp(new THREE.Color(palette.coral), incidentPhase * 0.08);
        }
      });
    }

    trafficDots.forEach((dot) => {
      const direction = incidentPhase > 0.28 ? 1 : Math.sin(motionTime * 0.8) > 0 ? 1 : -1;
      const t = (dot.userData.offset + motionTime * 0.18 * direction + 1) % 1;
      dot.position.set(lerp(-2.9, 2.9, t), Math.sin(t * Math.PI) * 0.34, 0.48);
    });

    streamGroup.rotation.z = Math.sin(motionTime * 0.12) * 0.035 + storyProgress * 0.09;
    streamGroup.position.y = filterReveal * 0.4;
    setGroupOpacity(streamGroup, lerp(0.92, 0.42, idcReveal));
    grid.material.opacity = 0.2 * (1 - smoothstep(0.82, 1, storyProgress));

    cyanLight.position.x = -4 + Math.sin(motionTime * 0.45) * 2.1;
    coralLight.position.y = -2 + Math.cos(motionTime * 0.38) * 1.4;
    interpolateCamera(storyProgress);
  }

  function animateCounters() {
    const counters = [...document.querySelectorAll("[data-count]")];
    const formatter = new Intl.NumberFormat("ko-KR", { notation: "compact", maximumFractionDigits: 0 });

    const observer = new IntersectionObserver(
      (entries) => {
        entries.forEach((entry) => {
          if (!entry.isIntersecting || entry.target.dataset.played) return;
          entry.target.dataset.played = "true";
          const target = Number(entry.target.dataset.count);
          const startTime = performance.now();
          const duration = reducedMotion ? 1 : 1350;

          function tick(now) {
            const progress = clamp((now - startTime) / duration);
            const eased = 1 - (1 - progress) ** 3;
            const value = Math.round(target * eased);
            entry.target.textContent = entry.target.dataset.format === "compact" ? formatter.format(value) : value.toLocaleString("ko-KR");
            if (progress < 1) requestAnimationFrame(tick);
          }
          requestAnimationFrame(tick);
        });
      },
      { threshold: 0.5 },
    );
    counters.forEach((counter) => observer.observe(counter));
  }

  function clearIncidentTimers() {
    incidentTimers.forEach((timer) => window.clearTimeout(timer));
    incidentTimers = [];
  }

  function activateProtocolStep(step) {
    protocolSteps.forEach((item, index) => item.classList.toggle("is-active", index < step));
  }

  function setIncidentState(step, status) {
    activateProtocolStep(step);
    if (incidentStatus) incidentStatus.textContent = status;
  }

  function runIncidentSimulation() {
    clearIncidentTimers();
    incidentRunning = true;
    targetIncidentPhase = 0.18;
    incidentButton?.classList.add("is-running");
    if (incidentButton) incidentButton.textContent = "전환 진행 중";
    idcAElement?.classList.add("is-down");
    if (idcAStrong) idcAStrong.textContent = "DEGRADED";
    setIncidentState(1, "IDC A 응답 지연 감지");

    const stages = [
      [650, 2, "Health Check 실패 확인", 0.38],
      [1350, 3, "IDC B로 트래픽 전환", 0.66],
      [2050, 4, "재시도 및 중복 이벤트 제거", 0.84],
      [2850, 5, "IDC B에서 서비스 정상화", 1],
    ];

    stages.forEach(([delay, step, status, phase], index) => {
      const timer = window.setTimeout(() => {
        targetIncidentPhase = phase;
        setIncidentState(step, status);
        if (index === stages.length - 1) {
          incidentRunning = false;
          if (idcAStrong) idcAStrong.textContent = "STANDBY";
          incidentButton?.classList.remove("is-running");
          if (incidentButton) incidentButton.textContent = "다시 실행";
        }
      }, reducedMotion ? delay * 0.05 : delay);
      incidentTimers.push(timer);
    });
  }

  incidentButton?.addEventListener("click", () => {
    if (incidentRunning) return;
    runIncidentSimulation();
  });

  window.addEventListener(
    "pointermove",
    (event) => {
      pointerX = (event.clientX / window.innerWidth - 0.5) * 2;
      pointerY = (event.clientY / window.innerHeight - 0.5) * 2;
    },
    { passive: true },
  );

  window.addEventListener("scroll", updateScrollState, { passive: true });
  window.addEventListener("resize", () => {
    camera.aspect = window.innerWidth / window.innerHeight;
    camera.updateProjectionMatrix();
    renderer.setPixelRatio(Math.min(window.devicePixelRatio || 1, 1.8));
    renderer.setSize(window.innerWidth, window.innerHeight);
    updateScrollState();
  });

  const clock = new THREE.Clock();

  function render() {
    const elapsed = clock.getElapsedTime();
    updateParticles(elapsed);
    updateScene(elapsed);
    renderer.render(scene, camera);
    requestAnimationFrame(render);
  }

  updateScrollState();
  animateCounters();
  render();

  window.__systemTour = {
    renderer,
    scene,
    camera,
    getState: () => ({ activeScene, scrollProgress, storyProgress, incidentPhase }),
    runIncidentSimulation,
  };
})();
