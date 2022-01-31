import React from 'react';
import 'App.css';
import Routes from 'Routes';
import TemplateLayout from 'template/TemplateLayout';
import { Helmet } from 'react-helmet-async';

const App = () => {
  return (
    <div className="App">
      <Helmet>
        <title>API Monitoring</title>
      </Helmet>
      <TemplateLayout>
        <Routes />
      </TemplateLayout>
    </div>
  );
};

export default App;
