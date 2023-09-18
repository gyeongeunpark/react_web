import { Route, Routes } from "react-router-dom";
import Footer from "./component/common/Footer";
import Header from "./component/common/Header";
import Join from "./component/member/Join";
import Login from "./component/member/Login";
import { useState } from "react";

function App() {
  //기본은 로그인이 안된 상태
  const [isLogin, setIsLogin] = useState(false);
  return (
    <div className="wrap">
      <Header isLogin={isLogin} setIsLogin={setIsLogin} />
      <div className="content">
        <Routes>
          <Route path="/join" element={<Join />} />
          <Route path="/login" element={<Login setIsLogin={setIsLogin} />} />
        </Routes>
      </div>
      <Footer />
    </div>
  );
}

export default App;
