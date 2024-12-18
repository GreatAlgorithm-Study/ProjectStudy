
import 'assets/App.css';
import { useEffect } from "react";
import axios from "axios";

export function KakaoRedirect() {
  const code = new URL(window.location.href).searchParams.get("code");

    // 인가코드를 백엔드에 보내주고, 토큰 받아오기
    // 인가코드로 카카오 로그인 요청 (Service Client -> Service Server)
    //인가코드 백으로 보내는 코드
    useEffect(() => {
        const kakaoLogin = async () => {
        await axios({
            method: "GET",
            url: `${process.env.REACT_APP_API_HOST}/auth/kakao?code=${code}`,
            headers: {
            "Content-Type": "application/json;charset=utf-8", //json형태로 데이터를 보내겠다는뜻
            "Access-Control-Allow-Origin": "*", //이건 cors 에러때문에 넣어둔것. 당신의 프로젝트에 맞게 지워도됨
            },
        }).then((res) => { //백에서 완료후 우리사이트 전용 토큰 넘겨주는게 성공했다면
            console.log(res);
            //계속 쓸 정보들( ex: 이름) 등은 localStorage에 저장해두자
            localStorage.setItem("name", res.data.account.kakaoName);
            //로그인이 성공하면 이동할 페이지
            // navigate("/owner-question");
        });
        };
        kakaoLogin();
    }, []);

  return (
    <div id = "commonLayoutContainer" style={{height: "99.7vh"}}>
        <div className = "_appbar_regc8_9">TalkTalk</div>
        <h1>로그인 중입니다.</h1>
    </div>
  );
}
export default KakaoRedirect;