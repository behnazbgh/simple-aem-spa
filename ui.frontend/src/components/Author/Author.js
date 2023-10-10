//import React, { Component } from 'react';
//import { MapTo } from '@adobe/aem-react-editable-components';
//
//export const CustomEditConfig = {
//  emptyLabel: 'Author',
//  isEmpty: function (props) {
//    const isEmpty = !props || !props.firstName || props.firstName.trim().length <1;
//    console.log("props is :", props);
//    if (isEmpty) {
//      console.log("Author component is empty:", props);
//    }
//    return isEmpty;
//  }
//};
//
//export default class Author extends Component {
//  render() {
//    if (CustomEditConfig.isEmpty(this.props)) {
//      return null;
//    }
//
//    return (
//      <div className="AuthorComponent">
//        <h2>{this.props.firstName}</h2>
//        <h2>{this.props.lastName}</h2>
//        <h2>{this.props.isAemDeveloper ? this.props.firstName+" is AEM Developer" : this.props.firstName +" is not AEM Developer"}</h2>
//      </div>
//    );
//  }
//}
//
////MapTo('wknd-spa-react/components/author')(Author, CustomEditConfig);
