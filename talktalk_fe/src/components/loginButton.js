import 'components/loginButton.css';

function loginButton({type, logoUrl}) {
    
    const KAKAO_REST_API_KEY = process.env.REACT_APP_KAKAO_REST_API_KEY
    const KAKAO_REDIRECT_URI = `http://localhost:3000/oauth`

    const getOauthUrl = (type) => {
        switch (type) {
            case 'kakao':
                return {
                    url: `https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=${KAKAO_REST_API_KEY}&redirect_uri=${KAKAO_REDIRECT_URI}`,
                    text: "카카오 로그인"
                };
            case 'google': {
                return {
                    url: ``,
                    text: "Google 계정으로 로그인"
                }
            }
            default: return {
                url: '/',
                text: "default"
            };
        }
    }
        
        
    const handleLoginClick = () => {
        const loginInfo = getOauthUrl(type);
        const url = loginInfo.url;
        
        // RedirectURI: https://localhost:3000/auth?code=xxx로 접속됨
        window.location.href = url;
        // 현재의 사이트에서 code 뒷부분만 파싱함
        // 인가코드 넘겨주고 AccessToken 받아와야 됨
        const code = new URL(document.location.toString()).searchParams.get('code');
        console.log(code);
    };
    
    
    const loginInfo = getOauthUrl(type);
    
    return (
        <button className= {`${type} + oauthLoginButton`} onClick={handleLoginClick}>
            <img className = "loginButtonLogo" src={logoUrl} alt="Google Logo"/>            
            <span className = "loginText"> {loginInfo.text} </span>
        </button>
    );
}

export default loginButton;