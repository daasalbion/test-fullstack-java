import React, {useEffect, useState} from "react";
import {Link, Route, Routes} from "react-router-dom";
import "bootstrap/dist/css/bootstrap.min.css";
import 'bootstrap-icons/font/bootstrap-icons.css';
import "./App.css";

import * as AuthService from "./services/auth.service";
import IUser from './types/user.type';

import Login from "./components/Login";
import Profile from "./components/Profile";
import BoardUser from "./components/BoardUser";

import EventBus from "./common/EventBus";
import CreateUserForm from "./components/CreateUserForm";
import EditUserForm from "./components/EditUserForm";

const App: React.FC = () => {
  const [currentUser, setCurrentUser] = useState<IUser | undefined>(undefined);

  useEffect(() => {
    const user = AuthService.getCurrentUser();

    if (user) {
      setCurrentUser(user);
    }

    EventBus.on("logout", logOut);

    return () => {
      EventBus.remove("logout", logOut);
    };
  }, []);

  const logOut = () => {
    AuthService.logout();
    setCurrentUser(undefined);
  };

  return (
    <div>
      <nav className="navbar navbar-expand navbar-dark bg-dark">
        <Link to={"/"} className="navbar-brand">
          DAAS
        </Link>
        <div className="navbar-nav mr-auto">
          {currentUser && (
            <li className="nav-item">
              <Link to={"/user"} className="nav-link">
                User
              </Link>
            </li>
          )}
        </div>

        {currentUser ? (
          <div className="navbar-nav ml-auto">
            <li className="nav-item">
              <Link to={"/profile"} className="nav-link">
                {currentUser.username}
              </Link>
            </li>
            <li className="nav-item">
              <a href="/" className="nav-link" onClick={logOut}>
                LogOut
              </a>
            </li>
          </div>
        ) : (
          <div className="navbar-nav ml-auto">
            <li className="nav-item">
              <Link to={"/login"} className="nav-link">
                Login
              </Link>
            </li>
          </div>
        )}
      </nav>

      <div className="container mt-3">
        <Routes>
          <Route path="/" element={<BoardUser />} />
          <Route path="/login" element={<Login />} />
          <Route path="/profile" element={<Profile />} />
          <Route path="/createUser" element={<CreateUserForm />} />
          <Route path="/editUser/:id" element={<EditUserForm />} />
        </Routes>
      </div>
    </div>
  );
};

export default App;
