# ISSUE 09 | REAL BOOK EDITION

An interactive career magazine for GitHub Pages. The portfolio is rendered as a physical 12-page book with a hard cover, two-page editorial spreads, paper curl, dynamic shadows, and a responsive single-page mobile mode.

## Structure

- Front cover
- Contents
- About
- Skill
- Achievements Summary
- Technical Leadership
- Career
- Project Detail, edited as a two-page feature
- Portfolio
- Closing Note
- Back cover

## Rendering

- `StPageFlip 2.0.7`: paper folding, hard-cover motion, drag/corner interaction, and page shadows
- `Three.js 0.185.1`: full-screen reading-room scene, physical lighting, depth, and pointer parallax
- Both libraries are stored in `assets/vendor/` so the GitHub Pages build does not depend on a runtime CDN request.

## Files

- `index.html`: semantic magazine pages and editorial content
- `styles.css`: book stage, page layouts, paper surfaces, and responsive typography
- `script.js`: page engine, chapter navigation, keyboard controls, hashes, image fallback, and 3D scene
- `assets/images/`: profile and portfolio artwork
- `assets/docs/lee-bowon-resume.pdf`: resume PDF
- `assets/docs/lee-bowon-portfolio.pdf`: portfolio PDF

## Interaction

- Drag or select a page corner to turn the paper.
- Use the circular previous/next controls or the left/right arrow keys.
- Select a desktop chapter index item to flip directly to that article.
- Mobile layouts switch to a single physical page while preserving the curl animation.

## Preview

```sh
python3 -m http.server 4173 --bind 127.0.0.1
```

Open `http://127.0.0.1:4173/`.

## GitHub Pages

Set the Pages publishing source to `issue-09-real-book` and `/ (root)`.
