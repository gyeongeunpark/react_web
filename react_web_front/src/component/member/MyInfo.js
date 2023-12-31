import axios from "axios";
import { Button1, Button2 } from "../util/Buttons";
import Input from "../util/InputFrm";
import "./myInfo.css";
import { useNavigate } from "react-router-dom";
import Swal from "sweetalert2";
const MyInfo = (props) => {
  const navigate = useNavigate();
  const member = props.member;
  const setMember = props.setMember;
  const setIsLogin = props.setIsLogin;
  //input 에서 받은 값을 setData에 넣었기때문에 (InputFrm) data로 가져옴
  const setMemberPhone = (data) => {
    member.memberPhone = data;
    setMember({ ...member });
  };
  const updateMemberPhone = () => {
    const token = window.localStorage.getItem("token");
    axios
      .post("/member/changePhone", member, {
        headers: {
          Authorization: "Bearer " + token,
        },
      })
      .then((res) => {
        Swal.fire({
          icon: "success",
          title: "전화번호가 수정되었습니다.",
        });
      })
      .catch((res) => {
        if (res.response.status === 403) {
          window.localStorage.removeItem("token");
          setIsLogin(false);
        }
      });
  };
  const deleteMember = () => {
    Swal.fire({
      icon: "warning",
      title: "회원 탈퇴",
      text: "회원을 탈퇴하시겠습니까?",
      showCancelButton: true,
      confirmButtonText: "탈퇴하기",
      cancelButtonText: "취소",
    }).then((res) => {
      if (res.isConfirmed) {
        const token = window.localStorage.getItem("token");
        axios
          .post("/member/delete", null, {
            headers: {
              Authorization: "Bearer " + token,
            },
          })
          .then((res) => {
            window.localStorage.removeItem("token");
            setIsLogin(false);
            Swal.fire({ title: "빠잉" });
          })
          .catch((res) => {
            if (res.response.status === 403) {
              console.log("로그인이 풀린상황");
              window.localStorage.removeItem("token");
              setIsLogin(false);
            }
          });
      } else {
        console.log("취소 누른 경우");
      }
    });
  };
  return (
    <div className="my-content-wrap">
      <div className="my-content-title">내정보</div>
      <table className="my-info-tbl">
        <tbody>
          <tr>
            <td>회원번호</td>
            <td>{member.memberNo}</td>
          </tr>
          <tr>
            <td>아이디</td>
            <td>{member.memberId}</td>
          </tr>
          <tr>
            <td>이름</td>
            <td>{member.memberName}</td>
          </tr>
          <tr>
            <td>전화번호</td>
            <td id="member-phone">
              <div>
                <Input
                  type="text"
                  data={member.memberPhone}
                  setData={setMemberPhone}
                  content="memberPhone"
                />
                <Button2 text="변경하기" clickEvent={updateMemberPhone} />
              </div>
            </td>
          </tr>
        </tbody>
      </table>
      <div className="delete-btn-box">
        <Button1 text="회원탈퇴" clickEvent={deleteMember} />
      </div>
    </div>
  );
};

export default MyInfo;
