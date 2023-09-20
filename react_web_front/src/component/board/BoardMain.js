import { Route, Routes } from "react-router-dom";
import "./board.css";
import BoardList from "./BoardList";
import BoardWrite from "./BoardWrite";
const BoardMain = (props) => {
  const isLogin = props.isLogin;
  const setIsLogin = props.setIsLogin;
  //route path="*" 이면 해당되지 않은 경로들 모두를 뜻함 -> 글쓰기, 상세보기 등등이 아니라면 무조건 list로 가게끔(맨 밑에 써줘야함)
  return (
    <div className="board-all-wrap">
      <div className="board-title">BOARD</div>
      <Routes>
        <Route path="write" element={<BoardWrite />} />
        <Route path="*" element={<BoardList isLogin={isLogin} />} />
      </Routes>
    </div>
  );
};

export default BoardMain;
