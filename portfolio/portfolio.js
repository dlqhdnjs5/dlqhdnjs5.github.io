const viewer = document.querySelector("#image-viewer");
const viewerImage = viewer?.querySelector("[data-viewer-image]");
const viewerCaption = viewer?.querySelector("[data-viewer-caption]");
const viewerClose = viewer?.querySelector("[data-viewer-close]");
let lastZoomTrigger = null;

function openViewer(image, caption) {
  if (!viewer || !viewerImage || !viewerCaption) return;

  viewerImage.src = image.currentSrc || image.src;
  viewerImage.alt = image.alt;
  viewerCaption.textContent = caption || image.alt;

  if (!viewer.open) viewer.showModal();

  viewerClose?.focus();
}

document.querySelectorAll(".project-page figure").forEach((figure) => {
  const image = figure.querySelector("img");

  if (!image) return;

  figure.classList.add("zoomable-media");
  image.draggable = false;

  const zoomButton = document.createElement("button");
  zoomButton.className = "media-zoom";
  zoomButton.type = "button";
  zoomButton.dataset.noPageFlip = "true";
  zoomButton.dataset.imageZoom = "true";
  zoomButton.setAttribute("aria-label", `${image.alt || "이미지"} 확대 보기`);
  zoomButton.innerHTML =
    "<span class=\"media-zoom-label\" aria-hidden=\"true\">＋ 확대</span>";
  figure.append(zoomButton);
});

window.__interactiveBook?.protectPageInteractiveElements();

function openZoomTarget(button) {
  const figure = button.closest("figure");
  const image = figure?.querySelector("img");

  if (!figure || !image) return false;

  lastZoomTrigger = button;
  openViewer(image, figure.querySelector("figcaption")?.textContent?.trim());
  return true;
}

document.addEventListener(
  "click",
  (event) => {
    const button =
      event.target instanceof Element ? event.target.closest("[data-image-zoom]") : null;

    if (!button) return;

    if (!openZoomTarget(button)) return;

    event.preventDefault();
    event.stopPropagation();
  },
  { capture: true },
);

document.addEventListener(
  "keydown",
  (event) => {
    const button =
      event.target instanceof Element ? event.target.closest("[data-image-zoom]") : null;

    if (!button || (event.key !== "Enter" && event.key !== " ")) return;
    if (!openZoomTarget(button)) return;

    event.preventDefault();
    event.stopPropagation();
  },
  { capture: true },
);

viewerClose?.addEventListener("click", () => viewer?.close());

viewer?.addEventListener("click", (event) => {
  if (event.target === viewer) viewer.close();
});

viewer?.addEventListener("close", () => {
  if (!viewerImage || !viewerCaption) return;
  viewerImage.removeAttribute("src");
  viewerCaption.textContent = "";
  lastZoomTrigger?.focus();
  lastZoomTrigger = null;
});
