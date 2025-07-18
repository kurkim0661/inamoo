<script>
  import { onMount } from "svelte";
  import { apiGet } from "../api.js";

  let payments = [];
  let loading = true;
  let error = "";

  onMount(async () => {
    try {
      payments = await apiGet("/payments");
    } catch (e) {
      error = "결제 정보를 불러오지 못했습니다.";
    } finally {
      loading = false;
    }
  });
</script>

<div class="p-8">
  <h1 class="text-2xl font-bold mb-6 text-gray-800">결제 관리</h1>
  <div class="overflow-x-auto">
    {#if loading}
      <div class="p-8 text-center text-gray-400">로딩 중...</div>
    {:else if error}
      <div class="p-8 text-center text-red-500">{error}</div>
    {:else}
      <table class="min-w-full bg-white rounded-xl shadow-md">
        <thead>
          <tr class="bg-indigo-50">
            <th class="py-3 px-4 text-left font-semibold text-gray-700">사용자</th>
            <th class="py-3 px-4 text-left font-semibold text-gray-700">상품</th>
            <th class="py-3 px-4 text-left font-semibold text-gray-700">금액</th>
            <th class="py-3 px-4 text-left font-semibold text-gray-700">결제일</th>
            <th class="py-3 px-4 text-left font-semibold text-gray-700">상태</th>
          </tr>
        </thead>
        <tbody>
          {#each payments as payment}
            <tr class="border-b hover:bg-indigo-50 transition">
              <td class="py-2 px-4">{payment.user}</td>
              <td class="py-2 px-4">{payment.product}</td>
              <td class="py-2 px-4">{payment.amount}</td>
              <td class="py-2 px-4">{payment.date}</td>
              <td class="py-2 px-4">
                <span class="px-2 py-1 rounded-full text-xs font-semibold {payment.status === '완료' ? 'bg-green-100 text-green-600' : 'bg-red-100 text-red-600'}">
                  {payment.status}
                </span>
              </td>
            </tr>
          {/each}
        </tbody>
      </table>
    {/if}
  </div>
</div>
