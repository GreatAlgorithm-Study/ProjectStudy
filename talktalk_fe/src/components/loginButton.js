import 'components/loginButton.css';

function loginButton({type, logoUrl}) {
    return (
        <button className= {`${type} + oauthLoginButton`}>
            <img className = "loginButtonLogo" src={logoUrl} alt="Google Logo"/>            
            <span className = "loginText"> {type} 계정으로 로그인</span>
        </button>
    )
}

export default loginButton;