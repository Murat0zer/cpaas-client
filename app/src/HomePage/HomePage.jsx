import React from 'react';
import {Link} from 'react-router-dom';
import {connect} from "react-redux";
import {userActions} from '../_actions/user.actions';
import {ChatBox} from "../ChatBox";
import {ContactList} from '../_components/ContactList'
import {AddContactButton} from "../_components/AddContactButton";
import {Col, Row} from "reactstrap";
import Container from "reactstrap/es/Container";
import {MyTextInput} from "../_components/MyTextInput";
import Header from "../_components/Header";
import {PaperSheet} from "../_components/PaperSheet";

class HomePage extends React.Component {

    componentDidMount() {
        const { user } = this.props;
        this.props.dispatch(userActions.getAll()); // get all contacs olur ileride.
    }

    handleDeleteUser(id) {
        return (e) => this.props.dispatch(userActions.delete(id));
    }

    render() {
        const {user} = this.props;
        return (
            <Container>
                <Row >
                    <Col xs={"12"}>
                    <Header/>
                    </Col>
                </Row>
                <Row>
                    <Col sm="12" md={{ size: 6, offset: 3 }}>
                        <h1>Hi {user.firstName}!</h1>
                        <p>You're logged in with React!!</p>

                        <p>
                            <Link to="/login">Logout</Link>
                        </p>
                    </Col>
                </Row>
                <Row>
                    <Col xs="4">
                        <Row>
                           <Col xs={'12'}>
                                <PaperSheet />
                           </Col>
                        </Row>

                        <Row>
                            <Col xs={'12'}>
                                <ContactList/>
                            </Col>
                        </Row>

                        <Row>
                            <Col xs="12">
                                <MyTextInput/>
                            </Col>
                        </Row>
                        <Row>
                            <Col xs ="12">
                                <AddContactButton/>
                            </Col>
                        </Row>
                    </Col>
                    <Col xs="8">
                        <ChatBox/>
                    </Col>
                </Row>
            </Container>
        );
    }
}

function mapStateToProps(state) {
    const {authentication, contacts} = state;
    const {user} = authentication;
    return {
        user,
        contacts
    };
}

const connectedHomePage = connect(mapStateToProps)(HomePage);
export {connectedHomePage as HomePage};