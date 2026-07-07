# LEE BOWON Portfolio

Static portfolio site for GitHub Pages.

## Files

- `index.html`: Main portfolio page
- `styles.css`: Responsive layout and scroll/3D styling
- `script.js`: Scroll motion and project detail modal
- `assets/docs/lee-bowon-resume.pdf`: Resume PDF
- `assets/docs/lee-bowon-portfolio.pdf`: Portfolio PDF
- `.nojekyll`: Prevents GitHub Pages from running Jekyll processing
- `.github/workflows/pages.yml`: GitHub Pages deployment workflow

## Deploy With GitHub Pages Actions

1. Create or connect a GitHub repository.
2. Push this project to the `master` branch.
3. Open the repository on GitHub.
4. Go to `Settings > Pages`.
5. Set `Build and deployment > Source` to `GitHub Actions`.
6. The workflow named `Deploy portfolio to GitHub Pages` will publish the site.

For a user site URL such as `https://dlqhdnjs5.github.io/`, create the repository as `dlqhdnjs5.github.io`.

For a project site, the URL will look like `https://dlqhdnjs5.github.io/<repository-name>/`.

## First Push Example

If the GitHub repository is empty, run commands like these from this folder:

```bash
git add .
git commit -m "Create portfolio site"
git branch -M master
git remote add origin https://github.com/dlqhdnjs5/dlqhdnjs5.github.io.git
git push -u origin master
```

Use another repository URL if you choose a project-site repository instead of `dlqhdnjs5.github.io`.

Or run the included PowerShell helper:

```powershell
.\scripts\deploy-github-pages.ps1
```

For a different repository URL:

```powershell
.\scripts\deploy-github-pages.ps1 -RepositoryUrl "https://github.com/dlqhdnjs5/<repository-name>.git"
```
