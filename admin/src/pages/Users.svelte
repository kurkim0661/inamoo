<script>
  import { onMount } from "svelte";
  import { apiGet } from "../api.js";

  let users = [];
  let loading = true;
  let error = "";

  onMount(async () => {
    try {
      users = await apiGet("/api/users");
    } catch (e) {
      error = "사용자 정보를 불러오지 못했습니다.";
    } finally {
      loading = false;
    }
  });
</script>

<div class="p-8">
  <h1 class="text-2xl font-bold mb-6 text-gray-800">사용자 관리</h1>
  <div class="overflow-x-auto">
    {#if loading}
      <div class="p-8 text-center text-gray-400">로딩 중...</div>
    {:else if error}
      <div class="p-8 text-center text-red-500">{error}</div>
    {:else}
      <table class="min-w-full bg-white rounded-xl shadow-md">
        <thead>
          <tr class="bg-indigo-50">
            <th class="py-3 px-4 text-left font-semibold text-gray-700">이름</th>
            <th class="py-3 px-4 text-left font-semibold text-gray-700">이메일</th>
            <th class="py-3 px-4 text-left font-semibold text-gray-700">권한</th>
            <th class="py-3 px-4 text-left font-semibold text-gray-700">상태</th>
            <th class="py-3 px-4"></th>
          </tr>
        </thead>
        <tbody>
          {#each users as user}
            <tr class="border-b hover:bg-indigo-50 transition">
              <td class="py-2 px-4">{user.name}</td>
              <td class="py-2 px-4">{user.email}</td>
              <td class="py-2 px-4">{user.role}</td>
              <td class="py-2 px-4">
                <span class="px-2 py-1 rounded-full text-xs font-semibold {user.status === '활성' ? 'bg-green-100 text-green-600' : 'bg-gray-200 text-gray-500'}">
                  {user.status}
                </span>
              </td>
              <td class="py-2 px-4 flex gap-2">
                <button class="text-indigo-600 hover:underline text-sm">수정</button>
                <button
                  class="text-red-600 hover:underline text-sm"
                  on:click={async () => {
                    if (confirm('정말로 이 사용자를 삭제하시겠습니까?')) {
                      try {
                        await fetch(`/api/users/${user.id}`, { method: 'DELETE' });
                        users = users.filter(u => u.id !== user.id);
                      } catch (e) {
                        alert('삭제에 실패했습니다.');
                      }
                    }
                  }}
                >삭제</button>
              </td>
            </tr>
          {/each}
        </tbody>
      </table>
    {/if}
  </div>
</div>
