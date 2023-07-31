import React, {useEffect, useState} from "react";

import {deleteUser, getUserBoard} from "../services/user.service";
import EventBus from "../common/EventBus";
import {Link, NavigateFunction, useNavigate} from "react-router-dom";
import {Field, Form, Formik} from "formik";
import {Filter} from "../common/User";
import * as AuthService from "../services/auth.service";

const initialValue: Filter = {
    fullName: '',
    email: '',
    status: 'ACTIVO'
};

const BoardUser: React.FC = () => {
    let navigate: NavigateFunction = useNavigate();

    const [users, setUsers] = useState<any[]>([]);
    const [isAdmin, setIsAdmin] = useState<boolean>(false);

    useEffect(() => {
        handleSearch(initialValue);
        const user = AuthService.getCurrentUser();

        if (user) {
            setIsAdmin(user.roles.includes("ROLE_ADMIN"));
        }
    }, []);

    const handleDelete = (id: number) => {
        deleteUser(id).then(() => {
            navigate("/");
            window.location.reload();
        });
    }

    const handleReset = () => handleSearch(initialValue);

    const handleSearch = (search: Filter) => {
        getUserBoard(search).then(
            (response) => {
                setUsers(response.data.content);
            },
            (error) => {
                if (error.response && (error.response.status === 401 || error.response.status == 403)) {
                    EventBus.dispatch("logout");
                    navigate("/login");
                }
            }
        );
    }

    return (
        <div className="container">
            <h1>Users
                {isAdmin && <Link to={"/createUser"} className="col">
                    <button className="btn btn-primary">
                        <span className="glyphicon glyphicon-edit" aria-hidden="true">Create</span>
                    </button>
                </Link>}
            </h1>
            <div>
                <Formik
                    initialValues={initialValue}
                    onSubmit={handleSearch}
                    enableReinitialize
                >
                    {
                        ({resetForm}) => {
                            return (
                                <Form>
                                    <div className="form-row">
                                        <div className="col">
                                            <Field name="fullName" type="text" className="form-control"
                                                   placeholder="Full Name"/>
                                        </div>
                                        <div className="col">
                                            <Field name="email" type="text" className="form-control"
                                                   placeholder="Email"/>
                                        </div>
                                        <div className="col">
                                            <Field as="select" name="status" type="text" className="form-control">
                                                <option value="ACTIVO">ACTIVO</option>
                                                <option value="INACTIVO">INACTIVO</option>
                                            </Field>
                                        </div>
                                    </div>
                                    <button type="submit" className="btn btn-primary">
                                        <span className="glyphicon glyphicon-edit" aria-hidden="true">Search</span>
                                    </button>
                                    <button type="reset" className="btn btn-warning"
                                            onClick={() => {
                                                resetForm();
                                                handleReset();
                                            }}>
                                        <span className="glyphicon glyphicon-edit" aria-hidden="true">Clear</span>
                                    </button>
                                </Form>
                            )
                        }
                    }
                </Formik>
            </div>
            <hr />
            <table className="table table-bordered table-striped table-centered">
                <thead>
                <tr>
                    <td>ID</td>
                    <td>Full Name</td>
                    <td>Email</td>
                    <td>Status</td>
                    <td>Role</td>
                    {isAdmin && <td>Actions</td>}
                </tr>
                </thead>
                <tbody>
                {users.map((i: any) => <tr key={i.id}>
                    <td>{i.id}</td>
                    <td>{i.fullName}</td>
                    <td>{i.email}</td>
                    <td>{i.status}</td>
                    <td>{i.role}</td>
                    {isAdmin && <td>
                        <Link to={`/editUser/${i.id}`}>
                            <button className="btn btn-primary editProject">
                                <i className="bi-pencil-square" aria-hidden="true"></i>
                            </button>
                        </Link>
                        <button className="btn btn-danger deleteProject" onClick={() => handleDelete(i.id)}>
                            <span className="bi-trash3" aria-hidden="true"></span>
                        </button>
                    </td>}
                </tr>)}
                </tbody>
            </table>
        </div>
    );
};

export default BoardUser;
