import React from 'react';
import ReactDOM from 'react-dom/client';
import App from './App';
import { BrowserRouter } from 'react-router-dom';

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
  // React Router 적용
  // App을 BrowserRouter로 감싸면서 프로젝트 페이지들을 URL경로에 따라 렌더링할 수 있도록 함
  // 컴포넌트에 Route로 라우팅시켜야됨
  <BrowserRouter>
    <App/>
  </BrowserRouter>
);
