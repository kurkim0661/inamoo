<script>
  import { onMount } from "svelte";
  import { apiGet } from "../api.js";

  let stats = [];
  let loading = true;
  let error = "";

  onMount(async () => {
    try {
      stats = await apiGet("/stats");
    } catch (e) {
      error = "대시보드 데이터를 불러오지 못했습니다.";
    } finally {
      loading = false;
    }
  });
</script>

<div class="p-8">
  <h1 class="text-3xl font-bold mb-6 text-gray-800">관리자 대시보드</h1>
  {#if loading}
    <div class="p-8 text-center text-gray-400">로딩 중...</div>
  {:else if error}
    <div class="p-8 text-center text-red-500">{error}</div>
  {:else}
    <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 mb-8">
      {#each stats as stat}
        <div class="bg-white rounded-xl shadow-md p-6 flex flex-col items-center hover:scale-105 transition-transform">
          <div class="text-2xl font-semibold text-indigo-600">{stat.value}</div>
          <div class="text-gray-500 mt-2">{stat.label}</div>
          <div class="text-sm mt-1 {stat.trend && stat.trend.startsWith('+') ? 'text-green-500' : 'text-red-500'}">{stat.trend}</div>
        </div>
      {/each}
    </div>
  {/if}
  <div class="bg-white rounded-xl shadow-md p-6">
    <h2 class="text-xl font-semibold mb-4 text-gray-700">트래픽 및 매출 추이</h2>
    <div class="h-48 flex items-center justify-center text-gray-400">
      <!-- 차트 라이브러리 연동 전, 임시 그래프 영역 -->
      <span>차트 영역 (추후 구현)</span>
    </div>
  </div>
</div>
