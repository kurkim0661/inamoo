## Brief overview
- Guidelines for developing e-commerce UI/UX and navigation logic, with a focus on a modern, wood-furniture themed shop. These rules are project-specific and reflect the design and workflow choices made for the inamoo project.

## Communication style
- Use direct, technical, and concise language when discussing code or requirements.
- Avoid unnecessary conversational filler; focus on actionable steps and clear explanations.

## UI/UX design principles
- Maintain a sophisticated, modern, and warm aesthetic suitable for a premium wood furniture e-commerce site.
- Use soft, natural color palettes (e.g., #f3ede2, #b48a5a, #7a7463) and elegant typography (e.g., 'Playfair Display', 'Noto Sans KR').
- Ensure all interactive elements (e.g., cart icon, buttons) have clear hover/focus states and subtle transitions for a premium feel.
- Keep layouts clean, with generous padding and rounded corners for containers and cards.
- Consistency with main page style is required for all new pages and components.

## Navigation and authentication logic
- All navigation icons in the navbar must have clear click handlers.
- For protected pages (e.g., cart, profile), always check authentication state before routing; redirect unauthenticated users to the login page.
- Use Svelte store (`user` from `$lib/stores/auth`) and `get(user)` for authentication checks.
- Use SvelteKit's `goto` for client-side navigation.

## Coding best practices
- All code must meet production-level quality standards, with thorough code review and maintainability in mind.
- Use consistent, project-wide conventions for naming, formatting, and structure.
- Use Svelte and SvelteKit idioms for component structure and routing.
- Place new route pages under `src/routes/` using SvelteKit's file-based routing.
- Use TypeScript for all Svelte components (`<script lang="ts">`).
- Use placeholder/mock data for UI prototyping, but clearly mark with comments for future replacement.
- Keep CSS modular and scoped within components; use media queries for responsive design.

## Naming conventions
- Use English for file and variable names, except for user-facing text which may be in Korean.
- Use kebab-case for filenames and directories, camelCase for variables and functions.

## Development workflow
- Implement new features in small, testable increments (e.g., create page, then add navigation, then add auth check).
- Always check the current state of the file after auto-formatting before making further changes.
- When adding new navigation or UI elements, ensure they are visually and functionally integrated with the existing design.
- Always write test code for new features and changes. Verification through tests is mandatory before considering a task complete.
  - Example: For SvelteKit, use Vitest or Playwright for component and integration tests.
  - Example: For backend, use JUnit or appropriate test frameworks.

## Other guidelines
- When creating or updating Cline rules, use succinct, descriptive filenames and organize by topic.
- Do not overwrite existing rule files; always create new ones for new guideline sets.
