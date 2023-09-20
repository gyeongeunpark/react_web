import Swal from "sweetalert2";
import { Button1, Button2, Button3 } from "../util/Buttons";
import Input from "../util/InputFrm";
import "./memberChangePw.css";
import { useState } from "react";
import axios from "axios";
const MemberChangePw = (props) => {
  //현재 비밀번호를 인증 했는지 안했는지 -> 초기에는 안했으므로 false
  const [isPwauth, setIsPwauth] = useState(false);
  const [currPw, setCurrPw] = useState("");
  const [memberPw, setMemberPw] = useState("");
  const [memberPwRe, setMemberPwRe] = useState("");
  const token = window.localStorage.getItem("token");
  const pwCheck = () => {
    axios
      .post(
        "/member/pwCheck",
        { memberPw: currPw },
        {
          headers: {
            Authorization: "Bearer " + token,
          },
        }
      )
      .then((res) => {
        if (res.data === 1) {
          setIsPwauth(true);
        } else {
          Swal.fire({
            title: "비밀번호를 다시 확인하세용",
          });
        }
      })
      .catch((res) => {});
  };
  const changePw = () => {
    if (memberPw === "" && memberPw !== memberPwRe) {
      Swal.fire({ title: "비밀번호를 확인하세요" });
    } else {
      axios
        .post(
          "/member/changePw",
          { memberPw: memberPw },
          {
            headers: {
              Authorization: "Bearer " + token,
            },
          }
        )
        .then((res) => {
          if (res.data === 1) {
            setIsPwauth(false);
            setCurrPw("");
            setMemberPw("");
            setMemberPwRe("");
            Swal.fire({ title: "비밀번호 변경 성공" });
          } else {
            Swal.fire(
              "비밀번호 변경 중 문제가 발생했습니다. 잠시 후 다시 시도해주세요"
            );
          }
        })
        .catch((res) => {
          if (res.response.status === 403) {
            window.localStorage.removeItem("token");
          }
        });
    }
  };
  return (
    <div className="my-content-wrap">
      <div className="my-content-title">비밀번호 변경</div>
      <div className="pw-auth">
        {isPwauth ? (
          <>
            <div className="new-pw-input-wrap">
              <div className="pw-input-wrap">
                <div>
                  <label htmlFor="memberPw">새 비밀번호</label>
                  <Input
                    type="password"
                    data={memberPw}
                    setData={setMemberPw}
                    content="memberPw"
                  />
                </div>
                <div>
                  <label htmlFor="memberPwRe">비밀번호 확인</label>
                  <Input
                    type="password"
                    data={memberPwRe}
                    setData={setMemberPwRe}
                    content="memberPwRe"
                  />
                </div>
              </div>
            </div>
            <div className="change-btn-box">
              <Button1 text="변경하기" clickEvent={changePw} />
            </div>
          </>
        ) : (
          <div className="pw-input-wrap">
            <div>
              <label htmlFor="currPw">현재 비밀번호</label>
              <Input
                data={currPw}
                setData={setCurrPw}
                type="password"
                content="currPw"
              />
              <Button2 text="입력" clickEvent={pwCheck} />
            </div>
          </div>
        )}
      </div>
    </div>
  );
};

export default MemberChangePw;
