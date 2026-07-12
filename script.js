import * as THREE from "./assets/vendor/three.module.min.js";

const bookElement = document.querySelector("#magazine-book");
const bookStage = document.querySelector("#book-stage");
const pageElements = [...document.querySelectorAll(".mag-page")];
const turnTargets = [...document.querySelectorAll("[data-turn-page]")];
const chapterButtons = [...document.querySelectorAll(".chapter-index [data-turn-page]")];
const previousButton = document.querySelector("#prev-page");
const nextButton = document.querySelector("#next-page");
const pageCounter = document.querySelector("#page-counter");
const progressFill = document.querySelector("#page-progress-fill");
const chapterKicker = document.querySelector("#chapter-kicker");
const chapterTitle = document.querySelector("#chapter-title");
const reducedMotion = window.matchMedia("(prefers-reduced-motion: reduce)").matches;

const pageMeta = pageElements.map((page, index) => ({
  index,
  title: page.dataset.pageName || `Page ${index + 1}`,
  kicker: page.dataset.pageKicker || "ISSUE 09",
  slug: page.dataset.pageSlug || `page-${index + 1}`,
}));

const slugToPage = new Map(pageMeta.map((page) => [page.slug, page.index]));
const pageFlipConstructor = window.St?.PageFlip;

if (!pageFlipConstructor) {
  throw new Error("PageFlip library failed to load.");
}

const hashSlug = window.location.hash.replace("#", "");
const initialPage = slugToPage.get(hashSlug) ?? 0;

const pageFlip = new pageFlipConstructor(bookElement, {
  width: 600,
  height: 760,
  size: "stretch",
  minWidth: 280,
  maxWidth: 620,
  minHeight: 355,
  maxHeight: 785,
  drawShadow: true,
  flippingTime: reducedMotion ? 320 : 1180,
  usePortrait: true,
  startZIndex: 10,
  startPage: initialPage,
  autoSize: true,
  maxShadowOpacity: 0.78,
  showCover: true,
  mobileScrollSupport: false,
  swipeDistance: 24,
  clickEventForward: true,
  useMouseEvents: true,
  disableFlipByClick: false,
});

function visiblePageLabel(index) {
  const orientation = pageFlip.getOrientation();
  const total = pageMeta.length;

  if (index === 0) return `COVER / ${total}`;
  if (index >= total - 1) return `BACK / ${total}`;

  if (orientation === "landscape") {
    const end = Math.min(index + 1, total - 1);
    return `${String(index).padStart(2, "0")}–${String(end).padStart(2, "0")} / ${total}`;
  }

  return `${String(index).padStart(2, "0")} / ${total}`;
}

function activeChapterPage(index) {
  if (index >= 9) return 9;
  if (index >= 7) return 7;
  return index;
}

function syncReader(index) {
  const safeIndex = Math.max(0, Math.min(index, pageMeta.length - 1));
  const meta = pageMeta[safeIndex];
  const chapterPage = activeChapterPage(safeIndex);

  chapterKicker.textContent = meta.kicker;
  chapterTitle.textContent = meta.title;
  pageCounter.textContent = visiblePageLabel(safeIndex);
  progressFill.style.width = `${((safeIndex + 1) / pageMeta.length) * 100}%`;
  previousButton.disabled = safeIndex === 0;
  nextButton.disabled = safeIndex >= pageMeta.length - 1;

  chapterButtons.forEach((button) => {
    const target = Number(button.dataset.turnPage);
    if (target === chapterPage) {
      button.setAttribute("aria-current", "page");
    } else {
      button.removeAttribute("aria-current");
    }
  });

  if (window.location.hash !== `#${meta.slug}`) {
    window.history.replaceState(null, "", `#${meta.slug}`);
  }
}

function turnToPage(target) {
  const pageIndex = Math.max(0, Math.min(Number(target), pageMeta.length - 1));
  const currentIndex = pageFlip.getCurrentPageIndex();

  if (pageIndex === currentIndex) return;

  const corner = pageIndex > currentIndex ? "bottom" : "top";
  pageFlip.flip(pageIndex, corner);
}

pageFlip.on("init", (event) => {
  document.body.classList.add("book-ready");
  document.body.dataset.orientation = event.data.mode;
  syncReader(event.data.page);
});

pageFlip.on("flip", (event) => {
  syncReader(event.data);
});

pageFlip.on("changeOrientation", (event) => {
  document.body.dataset.orientation = event.data;
  syncReader(pageFlip.getCurrentPageIndex());
});

pageFlip.on("changeState", (event) => {
  document.body.dataset.bookState = event.data;
});

pageFlip.loadFromHTML(document.querySelectorAll(".mag-page"));

turnTargets.forEach((button) => {
  button.addEventListener("click", (event) => {
    event.stopPropagation();
    turnToPage(button.dataset.turnPage);
  });
});

previousButton.addEventListener("click", () => pageFlip.flipPrev("top"));
nextButton.addEventListener("click", () => pageFlip.flipNext("bottom"));

window.addEventListener("keydown", (event) => {
  if (event.target instanceof HTMLInputElement || event.target instanceof HTMLTextAreaElement) return;

  if (event.key === "ArrowLeft" || event.key === "PageUp") {
    event.preventDefault();
    pageFlip.flipPrev("top");
  }

  if (event.key === "ArrowRight" || event.key === "PageDown" || event.key === " ") {
    event.preventDefault();
    pageFlip.flipNext("bottom");
  }

  if (event.key === "Home") {
    event.preventDefault();
    turnToPage(0);
  }

  if (event.key === "End") {
    event.preventDefault();
    turnToPage(pageMeta.length - 1);
  }
});

window.addEventListener("hashchange", () => {
  const target = slugToPage.get(window.location.hash.replace("#", ""));
  if (typeof target === "number" && target !== pageFlip.getCurrentPageIndex()) {
    turnToPage(target);
  }
});

document.querySelectorAll("img[data-fallback]").forEach((image) => {
  image.addEventListener("error", () => {
    if (image.src.endsWith(image.dataset.fallback)) return;
    image.src = image.dataset.fallback;
  });
});

function createReadingRoomScene() {
  const canvas = document.querySelector("#room-canvas");
  const renderer = new THREE.WebGLRenderer({ canvas, antialias: true, alpha: false });
  renderer.setPixelRatio(Math.min(window.devicePixelRatio, 1.75));
  renderer.setSize(window.innerWidth, window.innerHeight, false);
  renderer.shadowMap.enabled = true;
  renderer.shadowMap.type = THREE.PCFSoftShadowMap;
  renderer.outputColorSpace = THREE.SRGBColorSpace;
  renderer.toneMapping = THREE.ACESFilmicToneMapping;
  renderer.toneMappingExposure = 1.08;
  renderer.setClearColor(0x101315, 1);

  const scene = new THREE.Scene();
  scene.fog = new THREE.Fog(0x101315, 10, 31);

  const camera = new THREE.PerspectiveCamera(42, window.innerWidth / window.innerHeight, 0.1, 60);
  camera.position.set(0, 7.2, 12.5);
  camera.lookAt(0, -1.4, 0);

  const ambient = new THREE.HemisphereLight(0xddeef2, 0x20282b, 1.4);
  scene.add(ambient);

  const keyLight = new THREE.DirectionalLight(0xfff5e4, 3.2);
  keyLight.position.set(-5, 9, 6);
  keyLight.castShadow = true;
  keyLight.shadow.mapSize.set(1024, 1024);
  keyLight.shadow.camera.left = -12;
  keyLight.shadow.camera.right = 12;
  keyLight.shadow.camera.top = 12;
  keyLight.shadow.camera.bottom = -12;
  scene.add(keyLight);

  const cyanLight = new THREE.PointLight(0x2fd5df, 16, 18, 2);
  cyanLight.position.set(7, 1.5, -4);
  scene.add(cyanLight);

  const orangeLight = new THREE.PointLight(0xff7048, 12, 15, 2);
  orangeLight.position.set(-7, 0.5, 1);
  scene.add(orangeLight);

  const desk = new THREE.Mesh(
    new THREE.PlaneGeometry(36, 28),
    new THREE.MeshStandardMaterial({ color: 0x1b2022, roughness: 0.93, metalness: 0.04 }),
  );
  desk.rotation.x = -Math.PI / 2;
  desk.position.y = -2.25;
  desk.receiveShadow = true;
  scene.add(desk);

  const backWall = new THREE.Mesh(
    new THREE.PlaneGeometry(32, 16),
    new THREE.MeshStandardMaterial({ color: 0x171b1d, roughness: 0.88, metalness: 0.1 }),
  );
  backWall.position.set(0, 3.8, -9.5);
  backWall.receiveShadow = true;
  scene.add(backWall);

  const accentGroup = new THREE.Group();
  const accentGeometry = new THREE.BoxGeometry(5.6, 0.07, 0.12);
  const cyanMaterial = new THREE.MeshStandardMaterial({
    color: 0x2fd5df,
    emissive: 0x0c5960,
    emissiveIntensity: 1.3,
    roughness: 0.35,
  });
  const orangeMaterial = new THREE.MeshStandardMaterial({
    color: 0xff7048,
    emissive: 0x6d1d0c,
    emissiveIntensity: 1.05,
    roughness: 0.4,
  });

  const cyanBar = new THREE.Mesh(accentGeometry, cyanMaterial);
  cyanBar.position.set(5.3, -1.95, -3.8);
  cyanBar.rotation.y = -0.28;
  cyanBar.castShadow = true;
  accentGroup.add(cyanBar);

  const orangeBar = new THREE.Mesh(accentGeometry, orangeMaterial);
  orangeBar.position.set(-5.1, -2.02, 0.6);
  orangeBar.rotation.y = 0.34;
  orangeBar.castShadow = true;
  accentGroup.add(orangeBar);

  const frameMaterial = new THREE.MeshStandardMaterial({ color: 0x30383b, roughness: 0.5, metalness: 0.55 });
  const frameTop = new THREE.Mesh(new THREE.BoxGeometry(8.5, 0.09, 0.09), frameMaterial);
  const frameSide = new THREE.Mesh(new THREE.BoxGeometry(0.09, 4.8, 0.09), frameMaterial);
  frameTop.position.set(3.8, 5.4, -8.8);
  frameSide.position.set(8, 3, -8.8);
  accentGroup.add(frameTop, frameSide);
  scene.add(accentGroup);

  const pointerTarget = new THREE.Vector2(0, 0);
  const pointerCurrent = new THREE.Vector2(0, 0);
  let flipEnergy = 0;

  window.addEventListener("pointermove", (event) => {
    pointerTarget.x = (event.clientX / window.innerWidth - 0.5) * 2;
    pointerTarget.y = (event.clientY / window.innerHeight - 0.5) * 2;
  }, { passive: true });

  const resize = () => {
    camera.aspect = window.innerWidth / window.innerHeight;
    camera.updateProjectionMatrix();
    renderer.setSize(window.innerWidth, window.innerHeight, false);
    renderer.setPixelRatio(Math.min(window.devicePixelRatio, 1.75));
  };

  window.addEventListener("resize", resize, { passive: true });

  const renderFrame = () => {
    pointerCurrent.lerp(pointerTarget, reducedMotion ? 0.02 : 0.045);
    const isFlipping = document.body.dataset.bookState === "flipping";
    flipEnergy += ((isFlipping ? 1 : 0) - flipEnergy) * 0.08;

    camera.position.x = pointerCurrent.x * 0.28;
    camera.position.y = 7.2 - pointerCurrent.y * 0.16 + flipEnergy * 0.12;
    camera.lookAt(pointerCurrent.x * 0.12, -1.4, 0);
    accentGroup.rotation.y = pointerCurrent.x * 0.012;
    cyanMaterial.emissiveIntensity = 1.3 + flipEnergy * 0.9;
    orangeMaterial.emissiveIntensity = 1.05 + flipEnergy * 0.55;

    renderer.render(scene, camera);
    window.requestAnimationFrame(renderFrame);
  };

  renderFrame();
  return { renderer, scene, camera, resize };
}

let readingRoom3D;

try {
  readingRoom3D = createReadingRoomScene();
  document.body.classList.add("webgl-ready");
} catch (error) {
  document.body.classList.add("webgl-fallback");
  console.warn("WebGL scene unavailable; using the CSS reading room fallback.", error);
}

window.__issue09 = {
  pageFlip,
  readingRoom3D,
  turnToPage,
  getState: () => ({
    page: pageFlip.getCurrentPageIndex(),
    pageCount: pageFlip.getPageCount(),
    orientation: pageFlip.getOrientation(),
    bookState: document.body.dataset.bookState,
    webgl: document.body.classList.contains("webgl-ready"),
  }),
};
