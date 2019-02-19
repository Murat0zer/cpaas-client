import React from 'react';
import PropTypes from 'prop-types';
import {withStyles} from '@material-ui/core/styles';
import FormControl from '@material-ui/core/FormControl';
import Input from '@material-ui/core/Input';
import InputLabel from '@material-ui/core/InputLabel';
import {connect} from "react-redux";

const styles = theme => ({
    container: {
        display: 'flex',
        flexWrap: 'wrap',
    },
    formControl: {
        margin: theme.spacing.unit,
    },
});

class MyTextInput extends React.Component {
    state = {
        name: 'Enter contact information',
    };

    componentDidMount() {
        this.forceUpdate();
    }

    handleChange = event => {
        this.setState({name: event.target.value});
    };

    render() {
        const {classes} = this.props;

        return (
            <div  className={classes.container}>
                <FormControl style={{'width' : "100%"}} className={classes.formControl}>
                    <InputLabel  htmlFor="component-simple">Add Contact</InputLabel>
                    <Input id="component-simple" value={this.state.name} onChange={this.handleChange}/>
                </FormControl>
            </div>
        );
    }
}

MyTextInput.propTypes = {
    classes: PropTypes.object.isRequired,
};

function mapStateToProps(state) {
    const {authentication, contacts} = state;
    const {user} = authentication;
    return {
        user,
        contacts
    };
}

const connectedMyTextInput = (connect(mapStateToProps)(withStyles(styles)(MyTextInput)));
export {connectedMyTextInput as MyTextInput};
