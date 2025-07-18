<script>
  import Router from "svelte-spa-router";
  import routes from "./routes.js";
  import { onMount } from "svelte";

  // For active link highlighting
  let currentPath = "/";
  onMount(() => {
    currentPath = window.location.hash.replace("#", "") || "/";
    window.addEventListener("hashchange", () => {
      currentPath = window.location.hash.replace("#", "") || "/";
    });
  });

  const navLinks = [
    { path: "/dashboard", label: "대시보드", icon: "📊" },
    { path: "/users", label: "사용자 관리", icon: "👤" },
    { path: "/products", label: "상품 관리", icon: "🛒" },
    { path: "/payments", label: "결제 관리", icon: "💳" }
  ];
</script>

<div class="flex min-h-screen bg-gray-100">
  <!-- Sidebar -->
  <aside class="w-64 bg-white shadow-lg flex flex-col">
    <div class="h-20 flex items-center justify-center border-b">
      <span class="text-2xl font-extrabold text-indigo-600 tracking-tight">Inamoo Admin</span>
    </div>
    <nav class="flex-1 py-6">
      {#each navLinks as link}
        <a
          href={"#"+link.path}
          class="flex items-center px-6 py-3 mb-2 rounded-lg text-gray-700 font-medium hover:bg-indigo-50 transition
            {currentPath === link.path ? 'bg-indigo-100 text-indigo-700 font-bold' : ''}"
        >
          <span class="mr-3 text-xl">{link.icon}</span>
          {link.label}
        </a>
      {/each}
    </nav>
    <div class="p-6 border-t text-xs text-gray-400">
      © 2025 Inamoo Admin
    </div>
  </aside>

  <!-- Main Content -->
  <main class="flex-1 p-8">
    <Router {routes} />
  </main>
</div>
