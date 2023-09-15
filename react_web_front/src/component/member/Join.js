import { useState } from "react";
import "./join.css";
import Input from "../util/InputFrm";
const Join = () => {
  const [memberId, setMemberId] = useState("");
  const [memberPw, setMemberPw] = useState("");
  const [memberPwRe, setMemberPwRe] = useState("");
  const [memberName, setMemberName] = useState("");
  const [memberPhone, setMemberPhone] = useState("");
  const [checkIdMsg, setCheckIdMsg] = useState("");
  /* 
    component로 나누기 전에 했던 방법-> input이 5개면 이벤트함수도 5개 (다 적어야함) 
    -> component InputFrm()을 생성해서 input에 받는 값들을 전달해주는게 편함
  const changeMemberPw = (e) => {
    const inputPw = e.currentTarget.value;
    setMemberPw(inputPw);
  };
  const changeMemberId = (e) => {
    const inputId = e.currentTarget.value;
    setMemberId(inputId);
  };
  */
  return (
    <div className="join-wrap">
      <div className="join-title">회원가입</div>
      <JoinInputWrap
        data={memberId}
        setData={setMemberId}
        type="text"
        content="memberId"
        label="아이디"
        checkIdMsg={checkIdMsg}
      />
      <JoinInputWrap
        data={memberPw}
        setData={setMemberPw}
        type="password"
        content="memberPw"
        label="비밀번호"
      />
      <JoinInputWrap
        data={memberPwRe}
        setData={setMemberPwRe}
        type="password"
        content="memberPwRe"
        label="비밀번호 확인"
      />
      <JoinInputWrap
        data={memberName}
        setData={setMemberName}
        type="text"
        content="memberName"
        label="이름"
      />
      <JoinInputWrap
        data={memberPhone}
        setData={setMemberPhone}
        type="text"
        content="memberPhone"
        label="전화번호"
      />
    </div>
  );
};
const JoinInputWrap = (props) => {
  const data = props.data;
  const setData = props.setData;
  const type = props.type;
  const content = props.content;
  const label = props.label;
  const checkIdMsg = props.checkIdMsg;
  return (
    <div className="join-input-wrap">
      <div>
        <div className="label">
          <label htmlFor={content}>{label}</label>
        </div>
        <div className="input">
          <Input type={type} data={data} setData={setData} content={content} />
        </div>
      </div>
      <div></div>
    </div>
  );
};
export default Join;
