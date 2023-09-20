import { useState } from "react";
import BoardFrm from "./BoardFrm";
import Swal from "sweetalert2";
import axios from "axios";

const BoardWrite = () => {
  //제목,썸네일,내용,첨부파일 그외는 자동으로 -> 전송용 데이터를 담을 state
  const [boardTitle, setBoardTitle] = useState("");
  const [thumbnail, setThumbnail] = useState({}); //객체로 받아서 저장
  //첨부파일로 받아야하는데 변수명이 vo와 같으면 multipart쓸때 에러날수 있음 그래서 변수명 다르게 처리
  const [boardDetail, setBoardDetail] = useState("");
  const [boardFile, setBoardFile] = useState([]); //배열로 받아서 저장

  //boardImg -> 썸네일 미리보기용, fileList -> 첨부파일 목록 출력용(화면용 state)
  const [boardImg, setBoardImg] = useState("");
  const [fileList, setFileList] = useState([]);

  //글쓰기 버튼 클릭시 동작할 함수(서버에 insert요청함수)
  const write = () => {
    console.log(boardTitle);
    console.log(thumbnail);
    console.log(boardDetail);
    console.log(boardFile);
    if (boardTitle !== "" && boardDetail !== "") {
      //기본적인 문자열 또는 숫자데이터를 전송하는 경우에는 json을 전송
      //파일이 포함된 경우 -> FormData를 사용
      const form = new FormData();
      form.append("boardTitle", boardTitle);
      form.append("boardDetail", boardDetail);
      form.append("thumbnail", thumbnail); //첨부파일을 전송하는 경우 File 객체를 전송
      //첨부파일이 여러개인 경우(multiple인경우 -> 같은 이름으로 첨부파일이 여러개인 경우) for문이용 (첨부파일만 가능 문자열들은 for문 사용X)
      for (let i = 0; i < boardFile.length; i++) {
        form.append("boardFile", boardFile[i]);
      }
      //form에 데이터 저장했으므로 데이터보내주기
      const token = window.localStorage.getItem("token");
      axios
        .post("/board/insert", form, {
          headers: {
            contentType: "multipart/form-data",
            processData: false,
            Authorization: "Bearer " + token,
          },
        })
        .then((res) => {
          console.log(res.data);
        })
        .catch((res) => {
          console.log(res.response.status);
        });
    } else {
      Swal.fire("입력값을 확인하세요");
    }
  };
  return (
    <div>
      <div className="board-frm-title">게시글 작성</div>
      <BoardFrm
        boardTitle={boardTitle}
        setBoardTitle={setBoardTitle}
        boardDetail={boardDetail}
        setBoardDetail={setBoardDetail}
        thumbnail={thumbnail}
        setThumbnail={setThumbnail}
        boardFile={boardFile}
        setBoardFile={setBoardFile}
        boardImg={boardImg}
        setBoardImg={setBoardImg}
        fileList={fileList}
        setFileList={setFileList}
        buttonEvent={write}
        type="write"
      />
    </div>
  );
};

export default BoardWrite;
