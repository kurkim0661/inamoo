import type { RequestHandler } from '@sveltejs/kit';

export const POST: RequestHandler = async ({ request, locals }) => {
  try {
    const body = await request.json();
    // 실제로는 인증된 유저의 ID를 가져와야 함 (예: locals.user.id)
    // 여기서는 예시로 userId를 body에서 받는다고 가정
    const userId = body.userId;
    if (!userId) {
      return new Response(JSON.stringify({ message: '로그인이 필요합니다.' }), { status: 401 });
    }

    const res = await fetch(`${process.env.BACKEND_URL || 'http://localhost:8080'}/api/users/extra-info`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        userId,
        nickname: body.nickname,
        gender: body.gender,
        birthDate: body.birthDate,
        address: body.address,
        phoneNumber: body.phoneNumber,
        profileImage: body.profileImage
      })
    });

    if (!res.ok) {
      const data = await res.json();
      return new Response(JSON.stringify({ message: data.message || '백엔드 저장 실패' }), { status: 400 });
    }

    return new Response(JSON.stringify({ message: '저장 성공' }), { status: 200 });
  } catch (e) {
    return new Response(JSON.stringify({ message: '서버 오류' }), { status: 500 });
  }
};
