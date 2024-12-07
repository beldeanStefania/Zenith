import Modal from "react-modal";
import "./SingUp.css";

const SingUp = ({ show, setShow }: { show: boolean; setShow: Function }) => {
  return (
    <>
      <Modal
        isOpen={show}
        onRequestClose={() => setShow(false)}
        contentLabel="SingUp"
        overlayClassName="customOverlay"
        className="customModalSingUp"
      >
        <button 
          type="button"
          className="close"
          data-dismiss="modal"
          aria-label="Close"
          onClick={() => setShow(false)}
        >
          <span aria-hidden="true">&times;</span>
        </button>

        <div className="singup">
          <div className="form">
            <h1>Sign Up</h1>
            <form>
              <label htmlFor="exampleInputEmail1" className="position">
                Username
              </label>
              <div className="form-group">
                <input
                  type="text"
                  className="form-user"
                  id="fname"
                  aria-describedby="emailHelp"
                  placeholder="Enter First Name"
                />
                <input
                  type="text"
                  className="form-user2"
                  id="lname"
                  aria-describedby="emailHelp"
                  placeholder="Enter Last Name"
                />
              </div>
              <label htmlFor="exampleInputEmail1" className="position">
                Email address
              </label>
              <div className="form-group">
                <input
                  type="email"
                  className="form-control"
                  id="exampleInputEmail1"
                  aria-describedby="emailHelp"
                  placeholder="Enter email"
                />
              </div>
              <div className="form-group">
                <input
                  type="email"
                  className="form-control"
                  id="exampleInputEmail1"
                  aria-describedby="emailHelp"
                  placeholder="Confirm email"
                />
              </div>
              <label htmlFor="exampleInputPassword1" className="position">
                Password
              </label>
              <div className="form-group">
                <input
                  type="password"
                  className="form-control"
                  id="exampleInputPassword1"
                  placeholder="Password"
                />
              </div>
              <div className="form-group">
                <input
                  type="password"
                  className="form-control"
                  id="exampleInputPassword1"
                  placeholder="Confirm Password"
                />
              </div>

              <button type="submit" className="btn-singup">
                Sign In
              </button>
            </form>
            <a href="#" className="forgot">
              Already have an account? Sign In
            </a>
          </div>
        </div>
      </Modal>
    </>
  );
};

export default SingUp;
