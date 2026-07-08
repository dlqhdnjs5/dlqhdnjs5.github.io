const THREE = window.THREE;
const canvas = document.querySelector("#data-stream-canvas");
const progressLine = document.querySelector("#progress-line");
const progressValue = document.querySelector("#progress-value");

const scene = new THREE.Scene();
scene.fog = new THREE.FogExp2(0x02030a, 0.032);

const camera = new THREE.PerspectiveCamera(46, window.innerWidth / window.innerHeight, 0.1, 120);
camera.position.set(0, 8, 17);

const renderer = new THREE.WebGLRenderer({
  canvas,
  antialias: true,
  alpha: true,
  preserveDrawingBuffer: true,
});
renderer.setPixelRatio(Math.min(window.devicePixelRatio || 1, 2));
renderer.setSize(window.innerWidth, window.innerHeight);
renderer.setClearColor(0x02030a, 0);
renderer.outputColorSpace = THREE.SRGBColorSpace;

const streamGroup = new THREE.Group();
const cardGroup = new THREE.Group();
const orbitGuideGroup = new THREE.Group();
const branchGroup = new THREE.Group();
const projectGroup = new THREE.Group();
const particleGroup = new THREE.Group();

scene.add(streamGroup, orbitGuideGroup, cardGroup, branchGroup, projectGroup, particleGroup);

const ambient = new THREE.AmbientLight(0x85dfff, 0.72);
const keyLight = new THREE.PointLight(0x44efff, 2.4, 34);
keyLight.position.set(-4, 8, 8);
const magentaLight = new THREE.PointLight(0xff3fd4, 1.4, 38);
magentaLight.position.set(6, -8, 10);
scene.add(ambient, keyLight, magentaLight);

const palette = [0x44efff, 0x4e8cff, 0xb34cff, 0xff3fd4, 0x67ffbf];
const curves = [];
const cardMeshes = [];
const branchMaterials = [];
const orbitGuideMaterials = [];
const projectMeshes = [];
const particleCount = 210;
let scrollProgress = 0;
let targetProgress = 0;
let currentTravelY = 9.5;

const ORBIT_STOP = 0.42;
const SKILL_FADE_START = 0.36;
const SKILL_FADE_END = 0.44;
const STREAM_SPREAD_START = 0.39;
const STREAM_SPREAD_END = 0.55;
const DEEP_FADE_START = 0.86;
const DEEP_FADE_END = 0.98;

const skills = [
  {
    kicker: "PROFILE",
    title: "9년차 백엔드 개발자",
    body: "Java/Kotlin, Spring Boot, Kafka, MySQL, Redis 기반 운영 시스템 개발",
    meta: "LEE BOWON",
    color: "#44efff",
  },
  {
    kicker: "SCALE",
    title: "약 1억 건 Metric 처리",
    body: "불필요 Metric 약 9천만 건 필터링, 유효 Metric 약 1천만 건 저장",
    meta: "Metric Stream",
    color: "#67ffbf",
  },
  {
    kicker: "PERFORMANCE",
    title: "10초대 -> 0~1초대",
    body: "실행 계획과 조인 구조를 재설계해 운영 화면 응답성 개선",
    meta: "Query Tuning",
    color: "#4e8cff",
  },
  {
    kicker: "RELIABILITY",
    title: "IDC 이중화와 Failover",
    body: "Active-Active, Health Check, DR, Retry, 중복 방지 흐름 설계",
    meta: "HA / DR",
    color: "#ffe28a",
  },
  {
    kicker: "AI AUTOMATION",
    title: "RAG와 Tool Calling",
    body: "사내 가이드 챗봇, 코드리뷰 자동화, API 호출 에이전트 프로토타입",
    meta: "LLM App",
    color: "#ff3fd4",
  },
];

const projects = [
  {
    kicker: "PROJECT 01",
    title: "Metric Pipeline",
    body: "불필요 Metric 필터링과 저장 최적화",
    meta: "Kafka / MySQL",
    color: "#44efff",
  },
  {
    kicker: "PROJECT 02",
    title: "Incident Replay",
    body: "장애 감지, 복구, 회고를 연결하는 운영 흐름",
    meta: "HA / Monitoring",
    color: "#ff3fd4",
  },
  {
    kicker: "PROJECT 03",
    title: "AI Runbook",
    body: "LLM/RAG 기반 사내 지식 자동화",
    meta: "Agent / RAG",
    color: "#67ffbf",
  },
];

function clamp(value, min = 0, max = 1) {
  return Math.min(Math.max(value, min), max);
}

function smoothstep(edge0, edge1, value) {
  const x = clamp((value - edge0) / (edge1 - edge0));
  return x * x * (3 - 2 * x);
}

function streamPosition(lineIndex, total, t, expansion = 1) {
  const phase = lineIndex * 0.53;
  const normalized = lineIndex / Math.max(total - 1, 1) - 0.5;
  const y = -30 + t * 62;
  const waist = Math.sin(t * Math.PI);
  const splitTop = smoothstep(0.68, 1, t);
  const splitBottom = 1 - smoothstep(0, 0.22, t);
  const radius = 0.28 + waist * 1.55 + splitTop * 2.45 * expansion + splitBottom * 1.2;
  const twist = phase + t * 18.2 + Math.sin(t * 7 + phase) * 0.48;
  const fan = normalized * (splitTop * 7.8 * expansion - splitBottom * 4.8);
  const x = Math.cos(twist) * radius + fan;
  const z = Math.sin(twist) * radius * 0.72 + Math.sin(t * 9 + phase) * 0.22;

  return new THREE.Vector3(x, y, z);
}

function makeDataStream() {
  const lineTotal = 34;

  for (let i = 0; i < lineTotal; i += 1) {
    const points = [];
    for (let j = 0; j <= 160; j += 1) {
      points.push(streamPosition(i, lineTotal, j / 160, 0.82));
    }

    const curve = new THREE.CatmullRomCurve3(points);
    curves.push({ curve, index: i, total: lineTotal });

    const geometry = new THREE.TubeGeometry(curve, 180, i % 5 === 0 ? 0.018 : 0.011, 8, false);
    const baseOpacity = i % 4 === 0 ? 0.58 : 0.36;
    const material = new THREE.MeshBasicMaterial({
      color: palette[i % palette.length],
      transparent: true,
      opacity: baseOpacity,
      blending: THREE.AdditiveBlending,
      depthWrite: false,
    });

    const tube = new THREE.Mesh(geometry, material);
    tube.userData.baseOpacity = baseOpacity;
    streamGroup.add(tube);
  }
}

function makeBranchStream() {
  const lineTotal = 30;

  for (let i = 0; i < lineTotal; i += 1) {
    const points = [];
    const side = i / Math.max(lineTotal - 1, 1) - 0.5;
    const phase = i * 0.41;

    for (let j = 0; j <= 90; j += 1) {
      const t = j / 90;
      const y = -21 - t * 13;
      const x = side * 1.8 + side * t * 16 + Math.sin(t * 6 + phase) * 1.1;
      const z = Math.cos(t * 7 + phase) * (0.5 + t * 2.2);
      points.push(new THREE.Vector3(x, y, z));
    }

    const curve = new THREE.CatmullRomCurve3(points);
    const geometry = new THREE.TubeGeometry(curve, 80, 0.012, 8, false);
    const material = new THREE.MeshBasicMaterial({
      color: palette[(i + 2) % palette.length],
      transparent: true,
      opacity: 0,
      blending: THREE.AdditiveBlending,
      depthWrite: false,
    });
    branchMaterials.push(material);
    branchGroup.add(new THREE.Mesh(geometry, material));
  }
}

function roundRect(ctx, x, y, width, height, radius) {
  ctx.beginPath();
  ctx.moveTo(x + radius, y);
  ctx.arcTo(x + width, y, x + width, y + height, radius);
  ctx.arcTo(x + width, y + height, x, y + height, radius);
  ctx.arcTo(x, y + height, x, y, radius);
  ctx.arcTo(x, y, x + width, y, radius);
  ctx.closePath();
}

function wrapText(ctx, text, x, y, maxWidth, lineHeight, maxLines = 3) {
  const words = text.split(" ");
  const lines = [];
  let line = "";

  words.forEach((word) => {
    const testLine = line ? `${line} ${word}` : word;
    if (ctx.measureText(testLine).width > maxWidth && line) {
      lines.push(line);
      line = word;
    } else {
      line = testLine;
    }
  });

  if (line) lines.push(line);

  lines.slice(0, maxLines).forEach((item, index) => {
    ctx.fillText(item, x, y + index * lineHeight);
  });
}

function createCardTexture(card, wide = false) {
  const width = wide ? 1120 : 1160;
  const height = wide ? 520 : 560;
  const textureCanvas = document.createElement("canvas");
  textureCanvas.width = width;
  textureCanvas.height = height;
  const ctx = textureCanvas.getContext("2d");

  const gradient = ctx.createLinearGradient(0, 0, width, height);
  gradient.addColorStop(0, "rgba(36,52,86,0.98)");
  gradient.addColorStop(0.42, "rgba(12,18,39,0.98)");
  gradient.addColorStop(1, "rgba(4,8,20,0.99)");

  ctx.clearRect(0, 0, width, height);
  roundRect(ctx, 18, 18, width - 36, height - 36, 42);
  ctx.fillStyle = gradient;
  ctx.fill();
  ctx.lineWidth = 3;
  ctx.strokeStyle = card.color;
  ctx.globalAlpha = 0.72;
  ctx.stroke();
  ctx.globalAlpha = 1;

  ctx.fillStyle = card.color;
  ctx.font = "900 34px Inter, sans-serif";
  ctx.fillText(card.kicker, 70, 94);

  ctx.fillStyle = "#f8fbff";
  ctx.font = wide ? "900 72px Inter, sans-serif" : "900 70px Inter, sans-serif";
  wrapText(ctx, card.title, 70, wide ? 190 : 206, width - 140, wide ? 78 : 74, 2);

  ctx.fillStyle = "rgba(235,244,255,0.76)";
  ctx.font = wide ? "600 34px Inter, sans-serif" : "600 31px Inter, sans-serif";
  wrapText(ctx, card.body, 70, wide ? 318 : 372, width - 140, wide ? 44 : 42, 3);

  ctx.fillStyle = "rgba(255,255,255,0.08)";
  roundRect(ctx, 70, height - 104, wide ? 310 : 260, 54, 27);
  ctx.fill();
  ctx.fillStyle = card.color;
  ctx.font = "900 26px Inter, sans-serif";
  ctx.fillText(card.meta, 96, height - 68);

  const texture = new THREE.CanvasTexture(textureCanvas);
  texture.colorSpace = THREE.SRGBColorSpace;
  texture.anisotropy = renderer.capabilities.getMaxAnisotropy();
  return texture;
}

function createCardBackTexture(card, wide = false) {
  const width = wide ? 1120 : 1160;
  const height = wide ? 520 : 560;
  const textureCanvas = document.createElement("canvas");
  textureCanvas.width = width;
  textureCanvas.height = height;
  const ctx = textureCanvas.getContext("2d");

  const gradient = ctx.createLinearGradient(0, 0, width, height);
  gradient.addColorStop(0, "rgba(8,14,30,0.98)");
  gradient.addColorStop(0.58, "rgba(16,21,42,0.98)");
  gradient.addColorStop(1, "rgba(4,7,18,0.99)");

  ctx.clearRect(0, 0, width, height);
  roundRect(ctx, 18, 18, width - 36, height - 36, 42);
  ctx.fillStyle = gradient;
  ctx.fill();
  ctx.lineWidth = 3;
  ctx.strokeStyle = card.color;
  ctx.globalAlpha = 0.42;
  ctx.stroke();
  ctx.globalAlpha = 1;

  ctx.fillStyle = "rgba(255,255,255,0.06)";
  for (let x = 70; x < width - 70; x += 42) {
    ctx.fillRect(x, 82, 1, height - 164);
  }
  for (let y = 82; y < height - 82; y += 42) {
    ctx.fillRect(70, y, width - 140, 1);
  }

  ctx.fillStyle = card.color;
  ctx.font = "900 28px Inter, sans-serif";
  ctx.fillText("STREAM NODE", 70, 110);

  ctx.fillStyle = "rgba(248,251,255,0.92)";
  ctx.font = wide ? "900 58px Inter, sans-serif" : "900 56px Inter, sans-serif";
  wrapText(ctx, card.title, 70, height / 2 - 20, width - 140, 64, 2);

  ctx.fillStyle = "rgba(248,251,255,0.42)";
  ctx.font = "800 24px Inter, sans-serif";
  ctx.fillText(card.meta, 70, height - 84);

  const texture = new THREE.CanvasTexture(textureCanvas);
  texture.colorSpace = THREE.SRGBColorSpace;
  texture.anisotropy = renderer.capabilities.getMaxAnisotropy();
  return texture;
}

function makeCardMesh(card, wide = false) {
  const frontTexture = createCardTexture(card, wide);
  const backTexture = createCardBackTexture(card, wide);
  const width = wide ? 5.05 : 5.55;
  const height = wide ? 2.34 : 2.68;
  const depth = wide ? 0.16 : 0.18;
  const geometry = new THREE.BoxGeometry(width, height, depth, 1, 1, 1);
  const sideMaterial = new THREE.MeshBasicMaterial({
    color: new THREE.Color(card.color).multiplyScalar(0.28),
    transparent: true,
    opacity: 0,
    depthTest: true,
    depthWrite: false,
  });
  const frontMaterial = new THREE.MeshBasicMaterial({
    map: frontTexture,
    transparent: true,
    opacity: 0,
    depthTest: true,
    depthWrite: false,
  });
  const backMaterial = new THREE.MeshBasicMaterial({
    map: backTexture,
    transparent: true,
    opacity: 0,
    depthTest: true,
    depthWrite: false,
  });

  const materials = [
    sideMaterial,
    sideMaterial.clone(),
    sideMaterial.clone(),
    sideMaterial.clone(),
    frontMaterial,
    backMaterial,
  ];
  const mesh = new THREE.Mesh(geometry, materials);
  mesh.renderOrder = wide ? 22 : 18;
  const edgeGeometry = new THREE.EdgesGeometry(geometry);
  const edgeMaterial = new THREE.LineBasicMaterial({
    color: new THREE.Color(card.color),
    transparent: true,
    opacity: 0,
    depthTest: true,
    blending: THREE.AdditiveBlending,
  });
  const edge = new THREE.LineSegments(edgeGeometry, edgeMaterial);
  edge.renderOrder = mesh.renderOrder + 1;
  mesh.add(edge);
  mesh.userData.edge = edge;
  mesh.userData.materials = materials;
  return mesh;
}

function makeSkillCards() {
  const thetaStep = Math.PI * 0.7;
  const midTheta = ((skills.length - 1) * thetaStep) / 2;
  skills.forEach((card, index) => {
    const mesh = makeCardMesh(card);
    const theta = index * thetaStep;
    mesh.userData.theta = theta;
    mesh.userData.thetaStep = thetaStep;
    mesh.userData.midTheta = midTheta;
    mesh.userData.radius = index % 2 === 0 ? 6.15 : 5.65;
    mesh.userData.pitch = 0.92;
    mesh.userData.baseScale = 0.98 + (index % 2) * 0.04;
    mesh.userData.roll = index % 2 === 0 ? -0.035 : 0.035;
    cardMeshes.push(mesh);
    cardGroup.add(mesh);
  });
}

function makeOrbitGuides() {
  const guideCount = 3;
  const radius = 5.6;
  const pitch = 0.86;
  const thetaStart = -Math.PI * 3.4;
  const thetaEnd = Math.PI * 5.8;

  for (let i = 0; i < guideCount; i += 1) {
    const points = [];
    const phase = (i / guideCount) * Math.PI * 2;
    for (let j = 0; j <= 180; j += 1) {
      const t = j / 180;
      const theta = thetaStart + (thetaEnd - thetaStart) * t + phase;
      points.push(
        new THREE.Vector3(
          Math.cos(theta) * radius,
          (theta - (thetaStart + thetaEnd) / 2) * pitch,
          Math.sin(theta) * radius
        )
      );
    }

    const curve = new THREE.CatmullRomCurve3(points);
    const geometry = new THREE.TubeGeometry(curve, 160, 0.008, 6, false);
    const material = new THREE.MeshBasicMaterial({
      color: palette[(i + 1) % palette.length],
      transparent: true,
      opacity: 0.18,
      blending: THREE.AdditiveBlending,
      depthWrite: false,
    });
    orbitGuideMaterials.push(material);
    orbitGuideGroup.add(new THREE.Mesh(geometry, material));
  }
}

function makeProjectCards() {
  projects.forEach((card, index) => {
    const mesh = makeCardMesh(card, true);
    mesh.position.set((index - 1) * 5.15, -27.5 + Math.abs(index - 1) * 0.8, index === 1 ? -1.2 : 0.8);
    mesh.rotation.y = (index - 1) * -0.18;
    mesh.userData.stage = 0.73 + index * 0.045;
    projectMeshes.push(mesh);
    projectGroup.add(mesh);
  });
}

function makeParticles() {
  const geometry = new THREE.BufferGeometry();
  const positions = new Float32Array(particleCount * 3);
  const colors = new Float32Array(particleCount * 3);

  for (let i = 0; i < particleCount; i += 1) {
    const color = new THREE.Color(palette[i % palette.length]);
    colors[i * 3] = color.r;
    colors[i * 3 + 1] = color.g;
    colors[i * 3 + 2] = color.b;
  }

  geometry.setAttribute("position", new THREE.BufferAttribute(positions, 3));
  geometry.setAttribute("color", new THREE.BufferAttribute(colors, 3));

  const material = new THREE.PointsMaterial({
    size: 0.075,
    vertexColors: true,
    transparent: true,
    opacity: 0.95,
    blending: THREE.AdditiveBlending,
    depthWrite: false,
  });

  const points = new THREE.Points(geometry, material);
  particleGroup.add(points);
  return points;
}

function makeStarfield() {
  const geometry = new THREE.BufferGeometry();
  const count = 420;
  const positions = new Float32Array(count * 3);

  for (let i = 0; i < count; i += 1) {
    positions[i * 3] = (Math.random() - 0.5) * 44;
    positions[i * 3 + 1] = (Math.random() - 0.5) * 74;
    positions[i * 3 + 2] = -8 - Math.random() * 30;
  }

  geometry.setAttribute("position", new THREE.BufferAttribute(positions, 3));
  const material = new THREE.PointsMaterial({
    color: 0x8fcfff,
    size: 0.025,
    transparent: true,
    opacity: 0.48,
    blending: THREE.AdditiveBlending,
    depthWrite: false,
  });

  scene.add(new THREE.Points(geometry, material));
}

const particlePoints = makeParticles();
makeStarfield();
makeDataStream();
makeBranchStream();
makeOrbitGuides();
makeSkillCards();

function updateScrollProgress() {
  const maxScroll = Math.max(document.documentElement.scrollHeight - window.innerHeight, 1);
  targetProgress = clamp(window.scrollY / maxScroll);
  progressLine.style.height = `${Math.round(targetProgress * 100)}%`;
  progressValue.textContent = String(Math.round(targetProgress * 100)).padStart(2, "0");
}

function updateParticles(time) {
  const positions = particlePoints.geometry.attributes.position.array;
  const total = 34;
  const scrollDrive = clamp(scrollProgress / ORBIT_STOP);
  const spreadPhase = smoothstep(STREAM_SPREAD_START, STREAM_SPREAD_END, scrollProgress);

  for (let i = 0; i < particleCount; i += 1) {
    const lineIndex = i % total;
    const speed = 0.006 + (i % 7) * 0.001;
    const t = (scrollDrive * 0.26 + time * speed + i * 0.013) % 1;
    const point = streamPosition(lineIndex, total, t, 0.9 + spreadPhase * 0.85);
    positions[i * 3] = point.x;
    positions[i * 3 + 1] = point.y;
    positions[i * 3 + 2] = point.z;
  }

  particlePoints.geometry.attributes.position.needsUpdate = true;
}

function updateCards() {
  const mobile = window.innerWidth < 720;
  const radiusScale = mobile ? 0.72 : 1;
  const orbitDrive = clamp(scrollProgress / ORBIT_STOP);
  const spreadExit = smoothstep(SKILL_FADE_START, SKILL_FADE_END, scrollProgress);
  const orbitAdvance = orbitDrive * Math.PI * 6.35;

  cardMeshes.forEach((mesh) => {
    const theta = mesh.userData.theta + orbitAdvance;
    const radius = mesh.userData.radius * radiusScale + spreadExit * (mobile ? 2.3 : 5.2);
    const pitch = mesh.userData.pitch * (mobile ? 0.68 : 1);
    const helixY = (mesh.userData.theta - mesh.userData.midTheta) * pitch;
    const verticalDrift = Math.sin(theta * 0.72) * (mobile ? 0.24 : 0.42);
    const frontness = (Math.sin(theta) + 1) / 2;
    const sidePresence = 1 - Math.abs(Math.cos(theta));
    const alpha = clamp(0.38 + frontness * 0.5 + sidePresence * 0.15, 0.34, 1) * (1 - spreadExit);
    const scale =
      mesh.userData.baseScale *
      (mobile ? 0.66 : 1.05) *
      (0.88 + frontness * 0.18) *
      (1 - spreadExit * 0.18);

    mesh.position.set(
      Math.cos(theta) * radius,
      currentTravelY + helixY + verticalDrift - spreadExit * (mobile ? 1.8 : 3.2),
      Math.sin(theta) * radius - spreadExit * 2.4
    );
    mesh.rotation.set(0, Math.PI / 2 - theta, mesh.userData.roll);
    mesh.userData.materials.forEach((material) => {
      material.opacity = alpha;
    });
    mesh.userData.edge.material.opacity = alpha * 0.72;
    mesh.visible = alpha > 0.015;
    mesh.scale.setScalar(scale);
  });

  projectMeshes.forEach((mesh) => {
    const stage = mesh.userData.stage;
    const alpha = smoothstep(stage, stage + 0.08, scrollProgress);
    mesh.material.forEach((material) => {
      material.opacity = alpha;
    });
    mesh.userData.edge.material.opacity = alpha * 0.76;
    mesh.scale.setScalar(0.8 + alpha * 0.18);
    mesh.lookAt(camera.position);
  });

  const guideAlpha =
    smoothstep(0.04, 0.2, scrollProgress) * (1 - smoothstep(SKILL_FADE_START, SKILL_FADE_END, scrollProgress));
  orbitGuideMaterials.forEach((material, index) => {
    material.opacity = guideAlpha * (index === 0 ? 0.26 : 0.16);
  });
  orbitGuideGroup.position.y = currentTravelY;
  orbitGuideGroup.rotation.y = orbitAdvance;
}

function updateBranching() {
  const branchAlpha =
    smoothstep(STREAM_SPREAD_START, STREAM_SPREAD_END, scrollProgress) *
    (1 - smoothstep(DEEP_FADE_START, DEEP_FADE_END, scrollProgress));
  branchMaterials.forEach((material, index) => {
    material.opacity = branchAlpha * (index % 4 === 0 ? 0.72 : 0.42);
  });
}

function updateStreamPresence() {
  const spreadPhase = smoothstep(STREAM_SPREAD_START, STREAM_SPREAD_END, scrollProgress);
  const deepFade = 1 - smoothstep(DEEP_FADE_START, DEEP_FADE_END, scrollProgress);
  streamGroup.children.forEach((tube, index) => {
    const pulse = index % 5 === 0 ? 1.2 : 1;
    tube.material.opacity = tube.userData.baseOpacity * deepFade * (1 + spreadPhase * 0.35) * pulse;
  });
  particlePoints.material.opacity = 0.95 * deepFade;
}

function updateCamera() {
  const orbitDrive = clamp(scrollProgress / ORBIT_STOP);
  const spreadPhase = smoothstep(STREAM_SPREAD_START, STREAM_SPREAD_END, scrollProgress);
  const deepPhase = smoothstep(STREAM_SPREAD_END, 1, scrollProgress);
  const travelY = THREE.MathUtils.lerp(9.5, -9.2, orbitDrive);
  currentTravelY = travelY;
  const orbit = Math.sin(orbitDrive * Math.PI * 1.45) * 1.25 * (1 - spreadPhase * 0.35);
  const mobile = window.innerWidth < 720;
  const cameraY = travelY - spreadPhase * 9.8 - deepPhase * 4.5;

  camera.position.x = THREE.MathUtils.lerp(camera.position.x, mobile ? orbit * 0.38 : orbit, 0.06);
  camera.position.y = THREE.MathUtils.lerp(camera.position.y, cameraY, 0.07);
  camera.position.z = THREE.MathUtils.lerp(camera.position.z, mobile ? 18.5 : 15.8 + spreadPhase * 2.2, 0.06);

  const lookTarget = new THREE.Vector3(0, travelY - 1.8 - spreadPhase * 9.5, 0);
  camera.lookAt(lookTarget);

  streamGroup.rotation.y = orbitDrive * 2.35;
  cardGroup.rotation.y = 0;
  particleGroup.rotation.y = streamGroup.rotation.y;
  branchGroup.rotation.y = streamGroup.rotation.y * 0.8;
  projectGroup.rotation.y = 0;
}

function animate(timeMs = 0) {
  const time = timeMs * 0.001;
  scrollProgress += (targetProgress - scrollProgress) * 0.075;
  if (Math.abs(targetProgress - scrollProgress) < 0.0008) {
    scrollProgress = targetProgress;
  }

  keyLight.intensity = 2.2 + Math.sin(time * 1.6) * 0.22;
  magentaLight.intensity = 1.2 + smoothstep(0.58, 1, scrollProgress) * 1.2;

  updateParticles(time);
  updateCamera();
  updateCards();
  updateBranching();
  updateStreamPresence();

  renderer.render(scene, camera);
  requestAnimationFrame(animate);
}

function resize() {
  camera.aspect = window.innerWidth / window.innerHeight;
  camera.updateProjectionMatrix();
  renderer.setPixelRatio(Math.min(window.devicePixelRatio || 1, 2));
  renderer.setSize(window.innerWidth, window.innerHeight);
}

window.addEventListener("scroll", updateScrollProgress, { passive: true });
window.addEventListener("resize", resize);

updateScrollProgress();
animate();

window.__STREAM_PORTFOLIO_DEBUG__ = {
  get progress() {
    return scrollProgress;
  },
  get sceneObjects() {
    return {
      stream: streamGroup.children.length,
      skillCards: cardMeshes.length,
      branchLines: branchGroup.children.length,
      projectCards: projectMeshes.length,
    };
  },
  get scrollState() {
    return {
      scrollProgress,
      targetProgress,
      orbitDrive: clamp(scrollProgress / ORBIT_STOP),
      spreadExit: smoothstep(SKILL_FADE_START, SKILL_FADE_END, scrollProgress),
      visibleSkillCards: cardMeshes.filter((mesh) => mesh.visible).length,
      streamRotationY: Number(streamGroup.rotation.y.toFixed(4)),
      orbitGuideRotationY: Number(orbitGuideGroup.rotation.y.toFixed(4)),
    };
  },
  cardSnapshot() {
    return cardMeshes.map((mesh) => ({
      visible: mesh.visible,
      opacity: Number(mesh.userData.materials[4].opacity.toFixed(4)),
      x: Number(mesh.position.x.toFixed(3)),
      y: Number(mesh.position.y.toFixed(3)),
      z: Number(mesh.position.z.toFixed(3)),
      rotationY: Number(mesh.rotation.y.toFixed(4)),
    }));
  },
  samplePixels() {
    const gl = renderer.getContext();
    const width = gl.drawingBufferWidth;
    const height = gl.drawingBufferHeight;
    const size = 64;
    const pixels = new Uint8Array(size * size * 4);
    gl.readPixels(
      Math.max(0, Math.floor(width / 2 - size / 2)),
      Math.max(0, Math.floor(height / 2 - size / 2)),
      size,
      size,
      gl.RGBA,
      gl.UNSIGNED_BYTE,
      pixels
    );

    let litPixels = 0;
    let energy = 0;
    for (let i = 0; i < pixels.length; i += 4) {
      const value = pixels[i] + pixels[i + 1] + pixels[i + 2];
      energy += value;
      if (value > 24) litPixels += 1;
    }

    return {
      width,
      height,
      litPixels,
      energy,
      progress: scrollProgress,
    };
  },
};
