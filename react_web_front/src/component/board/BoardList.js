import axios from "axios";
import { useEffect, useState } from "react";
import Pagination from "../common/Pagination";
import { Button2 } from "../util/Buttons";

const BoardList = (props) => {
  const [boardList, setBoardList] = useState([]);
  const [reqPage, setReqPage] = useState(1);
  const [pageInfo, setPageInfo] = useState({});
  const isLogin = props.isLogin;
  //무한반복을 막으려면 useEffect
  useEffect(() => {
    //list는 로그인 안하고 봐도 되기 때문에 get으로 데이터 보내줌
    axios
      .get("/board/list/" + reqPage)
      .then((res) => {
        console.log(res.data);
        setBoardList(res.data.boardList);
        setPageInfo(res.data.pi);
      })
      .catch((res) => {
        console.log(res.response.status);
      });
  }, [reqPage]);
  //useEffect는 최초에 한번 시작하고 []배열 안의 값이 바뀔때마다 랜더링됨
  //클릭할때마다 setRepPage가 바뀌면 값이 바뀜->Pagination.js에서 이벤트 changePage만들기
  return (
    <div>
      {isLogin ? (
        <div className="board-write-btn">
          <Button2 text="글쓰기" />
        </div>
      ) : (
        ""
      )}
      <div className="board-list-wrap">
        {boardList.map((board, index) => {
          return <BoardItem key={"board" + index} board={board} />;
        })}
      </div>
      <div className="board-page">
        <Pagination
          reqPage={reqPage}
          setReqPage={setReqPage}
          pageInfo={pageInfo}
        />
      </div>
    </div>
  );
};
const BoardItem = (props) => {
  const board = props.board;
  return (
    <div className="board-item">
      <div className="board-item-img">
        {board.boardImg === null ? <img src="/image/default.png" /> : ""}
      </div>
      <div className="board-item-info">
        <div className="board-item-title">{board.boardTitle}</div>
        <div className="board-item-writer">{board.memberId}</div>
        <div className="board-item-date">{board.boardDate}</div>
      </div>
    </div>
  );
};

export default BoardList;
