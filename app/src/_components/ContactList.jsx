import React from 'react';
import PropTypes from 'prop-types';
import {withStyles} from '@material-ui/core/styles';
import List from '@material-ui/core/List';
import ListItem from '@material-ui/core/ListItem';
import ListItemIcon from '@material-ui/core/ListItemIcon';
import ListItemText from '@material-ui/core/ListItemText';
import Divider from '@material-ui/core/Divider';
import PersonIcon from '@material-ui/icons/Person';
import {connect} from "react-redux";
import {userActions} from "../_actions/user.actions";

const styles = theme => ({
    root: {
        width: '100%',
        maxWidth: 360,
        backgroundColor: theme.palette.background.paper,
        position: 'relative',
        overflow: 'auto',
        maxHeight: 450,
    },
});


class ContactList extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            selectedIndex: 1,
            selectedUser: ''
        };

    }

    componentDidMount() {
        const {user, contacts} = this.props;

        this.props.dispatch(userActions.getContacts(user.username));

        console.log(contacts)

    }

    handleListItemClick = (event, index) => {
        console.log('selected user:', `user${index + 1}`)
        this.setState({selectedIndex: index, selectedUser: `user${index + 1}`});
        
        this.props.dispatch(chatActions.setDestinationUser(this.state.selectedUser))

    };

    render() {
        const {classes, contacts, users} = this.props;

        return (
            <div className={classes.root}>
                {users.loading && <em>Loading users...</em>}

                {contacts &&
                <div>
                    {contacts.userNames.map((username, index) =>
                    <List component="nav">
                        <ListItem
                            button
                            selected={this.state.selectedIndex === index}
                            onClick={event => this.handleListItemClick(event, index)}
                        >
                            <ListItemIcon>
                                <PersonIcon/>
                            </ListItemIcon>
                            <ListItemText primary={username}/>
                        </ListItem>
                    </List>
                    )}
                </div>
                }
            </div>

        );
    }
}

ContactList.propTypes = {
    classes: PropTypes.object.isRequired,
};

function mapStateToProps(state) {
    const {authentication, users} = state;
    const {user} = authentication;
    const {contacts} = users;
    return {
        user,
        contacts,
        users,
    };
}

const connectedContactList = (connect(mapStateToProps)(withStyles(styles)(ContactList)));
export {connectedContactList as ContactList};
