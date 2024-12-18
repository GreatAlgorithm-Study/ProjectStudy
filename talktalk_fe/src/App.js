import 'assets/App.css';
import GoogleIcon from 'assets/image/google_logo.png'
import KakoIcon from 'assets/image/kakao_logo.png'
import LoginButton from "components/loginButton";

function App() {
  return (
    <div id = "commonLayoutContainer" style={{height: "99.7vh"}}>
      <div className = "_appbar_regc8_9">TalkTalk</div>

      <div style={{margin: "30vh auto"}}>
        <div className = "content" style={{textAlign: "center"}}>
          <LoginButton type={"google"} logoUrl={GoogleIcon}/>
        </div>
 
        <div className = "content" style={{textAlign: "center"}}>
          <LoginButton type={"kakao"} logoUrl={KakoIcon}/>
        </div>
      </div>
    </div>
  );
}

export default App;
