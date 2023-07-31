import React, {useEffect, useState} from "react";
import {getUser, updateUser} from "../services/user.service";
import {NavigateFunction, useNavigate, useParams} from "react-router-dom";
import * as Yup from "yup";
import {ErrorMessage, Field, Form, Formik} from "formik";
import {initialValues, User} from "../common/User";
import EventBus from "../common/EventBus";

const EditUserForm: React.FC = () => {

    let navigate: NavigateFunction = useNavigate();
    const { id } = useParams();
    const [loading, setLoading] = useState<boolean>(false);
    const [message, setMessage] = useState<string>("");
    const [values, setValues] = useState<User>(initialValues);

    useEffect(() => {
        getUser(id).then(
            (response) => {
                console.log(response.data);
                setValues(response.data);
            },
            (error) => {
                const _content =
                    (error.response &&
                        error.response.data &&
                        error.response.data.message) ||
                    error.message ||
                    error.toString();

                setValues(_content);

                if (error.response && error.response.status === 401) {
                    EventBus.dispatch("logout");
                }
            }
        )
    }, [id]);

    const validationSchema = Yup.object().shape({
        fullName: Yup.string().required("This field is required!"),
        email: Yup.string().required("This field is required!"),
        password: Yup.string().required("This field is required!"),
        status: Yup.string().required("This field is required!"),
        role: Yup.string().required("This field is required!")
    });

    const handleLogin = (formValue: User) => {
        setMessage("");
        setLoading(true);

        updateUser(id, formValue).then(
            () => {
                navigate("/");
                window.location.reload();
            },
            (error) => {
                const resMessage =
                    (error.response &&
                        error.response.data &&
                        error.response.data.message) ||
                    error.message ||
                    error.toString();

                setLoading(false);
                setMessage(resMessage);
            }
        );
    };

    console.log(values);

    return (
        <div className="container">
            <h3>Edit User</h3>
            <div className="col-md-12">
                <Formik
                    initialValues={values}
                    validationSchema={validationSchema}
                    onSubmit={handleLogin}
                    enableReinitialize
                >
                    <Form>
                        <div className="form-group">
                            <label htmlFor="fullName">FullName</label>
                            <Field name="fullName" type="text" className="form-control"/>
                            <ErrorMessage
                                name="fullName"
                                component="div"
                                className="alert alert-danger"
                            />
                        </div>
                        <div className="form-group">
                            <label htmlFor="email">Username</label>
                            <Field name="email" type="email" className="form-control"/>
                            <ErrorMessage
                                name="email"
                                component="div"
                                className="alert alert-danger"
                            />
                        </div>
                        <div className="form-group">
                            <label htmlFor="password">Password</label>
                            <Field name="password" type="password" className="form-control"/>
                            <ErrorMessage
                                name="password"
                                component="div"
                                className="alert alert-danger"
                            />
                        </div>
                        <div className="form-group">
                            <label htmlFor="status">Status</label>
                            <Field as="select" name="status" type="text" className="form-control">
                                <option value="ACTIVO">ACTIVO</option>
                                <option value="INACTIVO">INACTIVO</option>
                            </Field>
                            <ErrorMessage
                                name="status"
                                component="div"
                                className="alert alert-danger"
                            />
                        </div>
                        <div className="form-group">
                            <label htmlFor="role">Role</label>
                            <Field as="select" name="role" type="text" className="form-control">
                                <option value="ADMIN">ADMIN</option>
                                <option value="CONSULTOR">CONSULTOR</option>
                            </Field>
                            <ErrorMessage
                                name="role"
                                component="div"
                                className="alert alert-danger"
                            />
                        </div>
                        <div className="form-group">
                            <button type="submit" className="btn btn-primary btn-block" disabled={loading}>
                                {loading && (
                                    <span className="spinner-border spinner-border-sm"></span>
                                )}
                                <span>Update</span>
                            </button>
                        </div>
                        {message && (
                            <div className="form-group">
                                <div className="alert alert-danger" role="alert">
                                    {message}
                                </div>
                            </div>
                        )}
                    </Form>
                </Formik>
            </div>
        </div>
    );
};

export default EditUserForm;
