import "./buttons.css";
const Button1 = (props) => {
  const clickEvent = props.clickEvent;
  const text = props.text;
  return (
    <>
      <button className="btn style1" type="button" onClick={clickEvent}>
        {text}
      </button>
    </>
  );
};
const Button2 = (props) => {
  const clickEvent = props.clickEvent;
  const text = props.text;
  return (
    <>
      <button className="btn style2" type="button" onClick={clickEvent}>
        {text}
      </button>
    </>
  );
};
const Button3 = (props) => {
  const clickEvent = props.clickEvent;
  const text = props.text;
  return (
    <>
      <button className="btn style3" type="button" onClick={clickEvent}>
        {text}
      </button>
    </>
  );
};

export { Button1, Button2, Button3 };
//여러개 쓸 때는 default말고 {}안에 쓰고싶은 변수명 입력
