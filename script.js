const projects = {
  mukchoice: {
    title: "먹초이스 MukChoice",
    subtitle: "위치 기반 랜덤 맛집 추천 서비스",
    period: "2025.06.01 - 2025.09.14",
    hero: "https://github.com/user-attachments/assets/098dd4fb-74d5-4e9a-9fca-289b6c3671ad",
    links: [
      ["서비스", "https://mukchoice.kr"],
      ["GitHub", "https://github.com/dlqhdnjs5/MukChoice"],
    ],
    summary:
      "회사 점심시간의 '오늘 뭐 먹지?' 문제에서 출발한 서비스입니다. 위치 기반 검색, 그룹 위시리스트, 랜덤 추천을 결합해 선택 과정을 즐거운 경험으로 만들었습니다.",
    highlights: [
      ["Backend", "Kotlin, Spring Boot, JPA/Hibernate, MariaDB 기반 REST API와 데이터 모델링"],
      ["Auth", "Spring Security OAuth2 기반 카카오 로그인 인증/인가 구현"],
      ["Operation", "프론트/백 분리, Docker 기반 배포, Nginx Reverse Proxy 구조"],
    ],
    gallery: [
      "https://github.com/user-attachments/assets/e3a11b57-6e6e-4bb1-8cd7-e45b0c43826c",
      "https://github.com/user-attachments/assets/7ecdbb55-ab8f-4c98-b128-609373973484",
      "https://github.com/user-attachments/assets/8a68a834-3ba6-458a-8f1c-9353a9dd8fee",
      "https://github.com/user-attachments/assets/24fb3836-476c-4e47-96f2-ffad9d5f37fd",
      "https://github.com/user-attachments/assets/e0929fee-740e-4ba0-aeb1-dc705aa374a0",
      "https://github.com/user-attachments/assets/333eb8d5-905b-4db3-add0-ad843b3ebcd1",
    ],
  },
  nawago: {
    title: "나에게 와줘서 고마워 Nawago",
    subtitle: "공공 Open API 기반 유기동물 현황 공유 서비스",
    period: "2020.12.17 - 2021.02.12",
    hero: "https://user-images.githubusercontent.com/73633754/223451419-6f344c38-21ea-4b55-8dc9-f32d2a134675.png",
    links: [
      ["서비스", "https://nawago.net"],
      ["GitHub", "https://github.com/dlqhdnjs5/nawago_project"],
    ],
    summary:
      "공공 Open API 기반 유기동물 정보 조회와 사용자 커뮤니티 기능을 하나의 서비스로 통합했습니다. 인증, 게시글, 댓글, 좋아요, 이미지 업로드, AWS S3 저장까지 실제 서비스 흐름을 직접 구성했습니다.",
    highlights: [
      ["Architecture", "Spring Boot REST API와 Vue.js SPA를 분리한 구조"],
      ["Security", "Spring Security, JWT, x-auth 헤더 기반 Stateless 인증"],
      ["Data", "JPA, QueryDSL, MyBatis를 기능 특성에 따라 분리 적용"],
    ],
    gallery: [
      "https://github.com/dlqhdnjs5/nawago_project/assets/73633754/8887528b-e5f2-49cf-a606-3679d18c07d4",
      "https://github.com/dlqhdnjs5/nawago_project/assets/73633754/115bbe1b-b00e-4fec-a5a0-60cc002dcfa4",
      "https://github.com/dlqhdnjs5/nawago_project/assets/73633754/8a8285d3-e18c-4ac3-a2cf-4633be062dc1",
      "https://github.com/dlqhdnjs5/nawago_project/assets/73633754/630066c8-3c66-49db-bafa-cda02d381305",
      "https://github.com/dlqhdnjs5/nawago_project/assets/73633754/179ffdb5-51c7-4793-8093-c64a64fc2c8a",
      "https://github.com/dlqhdnjs5/nawago_project/assets/73633754/6921bb4c-bf1f-4bc1-8b90-dc4350194347",
    ],
  },
};

const revealObserver = new IntersectionObserver(
  (entries) => {
    entries.forEach((entry) => {
      if (entry.isIntersecting) {
        entry.target.classList.add("is-visible");
        revealObserver.unobserve(entry.target);
      }
    });
  },
  { threshold: 0.16 }
);

document.querySelectorAll(".reveal").forEach((element) => revealObserver.observe(element));

const story = document.querySelector(".scroll-story");
const systemMap = document.querySelector(".system-map");

function updateStoryMotion() {
  if (!story || !systemMap) return;

  const rect = story.getBoundingClientRect();
  const max = rect.height - window.innerHeight;
  const progress = Math.min(Math.max(-rect.top / max, 0), 1);
  const rotateX = 58 - progress * 42;
  const rotateZ = -18 + progress * 42;
  const translateY = `${progress * -26}px`;

  systemMap.style.setProperty("--map-x", `${rotateX}deg`);
  systemMap.style.setProperty("--map-z", `${rotateZ}deg`);
  systemMap.style.setProperty("--map-y", translateY);
}

function updateCursor(event) {
  document.documentElement.style.setProperty("--cursor-x", `${event.clientX}px`);
  document.documentElement.style.setProperty("--cursor-y", `${event.clientY}px`);
}

window.addEventListener("scroll", updateStoryMotion, { passive: true });
window.addEventListener("resize", updateStoryMotion);
window.addEventListener("pointermove", updateCursor, { passive: true });
updateStoryMotion();

const modal = document.querySelector("#project-modal");
const modalContent = document.querySelector("#modal-content");

function createProjectModal(project) {
  const links = project.links
    .map(
      ([label, href], index) =>
        `<a class="button ${index === 0 ? "primary" : "ghost"}" href="${href}" target="_blank" rel="noreferrer">${label}</a>`
    )
    .join("");

  const highlights = project.highlights
    .map(([title, body]) => `<article><h3>${title}</h3><p>${body}</p></article>`)
    .join("");

  const gallery = project.gallery
    .map((src) => `<img src="${src}" alt="${project.title} 프로젝트 이미지" loading="lazy" />`)
    .join("");

  return `
    <div class="modal-hero">
      <div>
        <p class="eyebrow">${project.period}</p>
        <h2>${project.title}</h2>
        <p class="hero-lede">${project.subtitle}</p>
        <p>${project.summary}</p>
        <div class="hero-actions">${links}</div>
      </div>
      <img src="${project.hero}" alt="${project.title} 대표 이미지" />
    </div>
    <div class="modal-list">${highlights}</div>
    <div class="modal-gallery">${gallery}</div>
  `;
}

function openModal(projectKey) {
  const project = projects[projectKey];
  if (!project || !modal || !modalContent) return;

  modalContent.innerHTML = createProjectModal(project);
  modal.classList.add("is-open");
  modal.setAttribute("aria-hidden", "false");
  document.body.style.overflow = "hidden";
}

function closeModal() {
  if (!modal || !modalContent) return;

  modal.classList.remove("is-open");
  modal.setAttribute("aria-hidden", "true");
  document.body.style.overflow = "";
  modalContent.innerHTML = "";
}

document.querySelectorAll("[data-project]").forEach((card) => {
  card.addEventListener("click", () => openModal(card.dataset.project));
  card.addEventListener("keydown", (event) => {
    if (event.key === "Enter" || event.key === " ") {
      event.preventDefault();
      openModal(card.dataset.project);
    }
  });
});

document.querySelectorAll("[data-close-modal]").forEach((button) => {
  button.addEventListener("click", closeModal);
});

window.addEventListener("keydown", (event) => {
  if (event.key === "Escape") closeModal();
});
