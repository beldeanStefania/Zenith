import Modal from "react-modal";
import "./LogIn.css";

const LogIn = ({
  showlog,
  setShowlog,
}: {
  showlog: boolean;
  setShowlog: Function;
}) => {
  return (
    <Modal
      isOpen={showlog} // Modalul se va deschide doar dacă showlog este true
      onRequestClose={() => setShowlog(false)} // Va închide modalul la click pe overlay sau butonul de închidere
      contentLabel="LogIn"
      overlayClassName="customOverlay"
      className="customModalLogIn"
    >
      <button
        type="button"
        className="close"
        data-dismiss="modal"
        aria-label="Close"
        onClick={() => setShowlog(false)} // Închide modalul la click
      >
        <span aria-hidden="true">&times;</span>
      </button>

      <div className="login">
        <div className="form">
          <h1>Log In</h1>
          <form>
            <label htmlFor="user" className="position">
              Username
            </label>
            <input
              type="text"
              className="form-user-login"
              id="user"
              placeholder="Enter Username"
            />
            <label htmlFor="password" className="position">
              Password
            </label>
            <input
              type="password"
              className="form-control-login"
              id="password"
              placeholder="Password"
            />
            <button type="submit" className="btn-login">
              Submit
            </button>
          </form>
          <a href="#" className="forgot">
            Forgot Password?
          </a>
        </div>
      </div>
    </Modal>
  );
};

export default LogIn;
