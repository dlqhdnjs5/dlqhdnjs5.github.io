(() => {
  const spreads = [...document.querySelectorAll(".spread[data-page]")];
  const pageTabs = [...document.querySelectorAll(".chapter-tabs [data-page-target]")];
  const pageTargets = [...document.querySelectorAll("[data-page-target]")];
  const previousButton = document.querySelector("#prev-page");
  const nextButton = document.querySelector("#next-page");
  const pageCounter = document.querySelector("#page-counter");
  const progressFill = document.querySelector("#page-progress-fill");
  const currentTitle = document.querySelector("#current-title");
  const currentKicker = document.querySelector("#current-kicker");
  const notesToggle = document.querySelector("#notes-toggle");
  const magazine = document.querySelector("#magazine");
  const reducedMotion = window.matchMedia("(prefers-reduced-motion: reduce)").matches;

  if (!spreads.length) return;

  let currentPage = 0;
  let touchStartX = 0;
  let touchStartY = 0;

  function clamp(value, min, max) {
    return Math.min(Math.max(value, min), max);
  }

  function pageFromHash() {
    const slug = window.location.hash.replace("#", "");
    if (!slug) return 0;
    const index = spreads.findIndex((spread) => spread.id === `page-${slug}`);
    return index >= 0 ? index : 0;
  }

  function updateHash(index) {
    const slug = spreads[index].id.replace("page-", "");
    const target = index === 0
      ? `${window.location.pathname}${window.location.search}`
      : `${window.location.pathname}${window.location.search}#${slug}`;
    window.history.replaceState(null, "", target);
  }

  function revealPage(index, options = {}) {
    const nextIndex = clamp(Number(index), 0, spreads.length - 1);
    const direction = nextIndex >= currentPage ? "forward" : "back";
    const samePage = nextIndex === currentPage;
    document.body.dataset.direction = direction;

    spreads.forEach((spread, spreadIndex) => {
      const active = spreadIndex === nextIndex;
      spread.classList.toggle("is-active", active);
      spread.setAttribute("aria-hidden", String(!active));
      spread.inert = !active;
      if (active) spread.scrollTop = 0;
    });

    pageTabs.forEach((tab) => {
      const active = Number(tab.dataset.pageTarget) === nextIndex;
      tab.setAttribute("aria-selected", String(active));
      tab.tabIndex = active ? 0 : -1;
      if (active) tab.scrollIntoView({ behavior: reducedMotion ? "auto" : "smooth", block: "nearest", inline: "center" });
    });

    const activeSpread = spreads[nextIndex];
    currentPage = nextIndex;
    currentTitle.textContent = activeSpread.dataset.title || "Issue 09";
    currentKicker.textContent = activeSpread.dataset.kicker || "LEE BOWON";
    pageCounter.textContent = `${String(nextIndex + 1).padStart(2, "0")} / ${String(spreads.length).padStart(2, "0")}`;
    progressFill.style.width = `${((nextIndex + 1) / spreads.length) * 100}%`;
    previousButton.disabled = nextIndex === 0;
    nextButton.disabled = nextIndex === spreads.length - 1;

    if (options.updateHash !== false) updateHash(nextIndex);

    if (options.focusHeading && !samePage) {
      window.setTimeout(() => {
        activeSpread.querySelector("h1, h2")?.focus({ preventScroll: true });
      }, reducedMotion ? 0 : 460);
    }
  }

  pageTargets.forEach((target) => {
    target.addEventListener("click", () => {
      revealPage(target.dataset.pageTarget, { focusHeading: target.closest(".contents-list") !== null });
    });
  });

  previousButton.addEventListener("click", () => revealPage(currentPage - 1, { focusHeading: true }));
  nextButton.addEventListener("click", () => revealPage(currentPage + 1, { focusHeading: true }));

  function setNotes(open) {
    document.body.classList.toggle("show-notes", open);
    notesToggle.setAttribute("aria-pressed", String(open));
  }

  notesToggle.addEventListener("click", () => {
    setNotes(!document.body.classList.contains("show-notes"));
  });

  const projectTabs = [...document.querySelectorAll(".project-switcher [data-project-target]")];
  const projectPanels = [...document.querySelectorAll(".project-panel[data-project]")];

  function revealProject(projectName, focusPanel = false) {
    projectTabs.forEach((tab) => {
      const active = tab.dataset.projectTarget === projectName;
      tab.setAttribute("aria-selected", String(active));
      tab.tabIndex = active ? 0 : -1;
    });

    projectPanels.forEach((panel) => {
      const active = panel.dataset.project === projectName;
      panel.classList.toggle("is-active", active);
      panel.setAttribute("aria-hidden", String(!active));
      panel.inert = !active;
      if (active && focusPanel) panel.querySelector("h3")?.focus({ preventScroll: true });
    });
  }

  projectTabs.forEach((tab) => {
    tab.addEventListener("click", () => revealProject(tab.dataset.projectTarget));
    tab.addEventListener("keydown", (event) => {
      if (!["ArrowLeft", "ArrowRight", "Home", "End"].includes(event.key)) return;
      event.preventDefault();
      event.stopPropagation();
      const currentIndex = projectTabs.indexOf(tab);
      let nextIndex = currentIndex;
      if (event.key === "ArrowLeft") nextIndex = currentIndex - 1;
      if (event.key === "ArrowRight") nextIndex = currentIndex + 1;
      if (event.key === "Home") nextIndex = 0;
      if (event.key === "End") nextIndex = projectTabs.length - 1;
      nextIndex = (nextIndex + projectTabs.length) % projectTabs.length;
      projectTabs[nextIndex].focus();
      revealProject(projectTabs[nextIndex].dataset.projectTarget);
    });
  });

  document.querySelector(".chapter-tabs")?.addEventListener("keydown", (event) => {
    if (!["ArrowLeft", "ArrowRight", "Home", "End"].includes(event.key)) return;
    event.preventDefault();
    let nextIndex = currentPage;
    if (event.key === "ArrowLeft") nextIndex = currentPage - 1;
    if (event.key === "ArrowRight") nextIndex = currentPage + 1;
    if (event.key === "Home") nextIndex = 0;
    if (event.key === "End") nextIndex = spreads.length - 1;
    nextIndex = clamp(nextIndex, 0, spreads.length - 1);
    pageTabs[nextIndex].focus();
    revealPage(nextIndex);
  });

  document.addEventListener("keydown", (event) => {
    const target = event.target;
    const editing = target instanceof HTMLElement && (
      target.isContentEditable || ["INPUT", "TEXTAREA", "SELECT"].includes(target.tagName)
    );
    if (editing || target.closest?.(".project-switcher")) return;

    if (event.key === "ArrowLeft") {
      event.preventDefault();
      revealPage(currentPage - 1, { focusHeading: true });
    }
    if (event.key === "ArrowRight") {
      event.preventDefault();
      revealPage(currentPage + 1, { focusHeading: true });
    }
    if (event.key.toLowerCase() === "n") {
      event.preventDefault();
      setNotes(!document.body.classList.contains("show-notes"));
    }
  });

  magazine.addEventListener("touchstart", (event) => {
    touchStartX = event.changedTouches[0].clientX;
    touchStartY = event.changedTouches[0].clientY;
  }, { passive: true });

  magazine.addEventListener("touchend", (event) => {
    const deltaX = event.changedTouches[0].clientX - touchStartX;
    const deltaY = event.changedTouches[0].clientY - touchStartY;
    if (Math.abs(deltaX) < 70 || Math.abs(deltaY) > Math.abs(deltaX) * 0.7) return;
    revealPage(deltaX < 0 ? currentPage + 1 : currentPage - 1);
  }, { passive: true });

  document.querySelectorAll("img[data-fallback]").forEach((image) => {
    image.addEventListener("error", () => {
      if (image.dataset.fallback && image.src !== image.dataset.fallback) {
        image.src = image.dataset.fallback;
      }
    }, { once: true });
  });

  window.addEventListener("hashchange", () => {
    const hashPage = pageFromHash();
    if (hashPage !== currentPage) revealPage(hashPage, { updateHash: false });
  });

  revealProject(projectTabs[0]?.dataset.projectTarget || "alert");
  currentPage = pageFromHash();
  revealPage(currentPage, { updateHash: false });

  window.__issue09 = {
    getCurrentPage: () => currentPage,
    goToPage: (index) => revealPage(index),
    showNotes: setNotes,
    selectProject: revealProject,
  };
})();
