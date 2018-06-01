App Components:
- notify
 + modular notify box
 + toggle + settings
 + global ref for all compnents to use
 + single css styles
+ check out https://github.com/mouse0270/bootstrap-notify

- navbar
 + modular contents

- navtop
 + login
  - API linking
  - result notify
 + search
 + home link

- app
 + overview
 + detail view
 + add

API:
- TODO
 + GET / PUT / DELETE / POST
 GET response: { todo : []} 

- LOGIN
 + POST 
  
  request: {
    name : name,
    password : pw,

  }

  response: {
    //JAVA login api things
    sessionId : id //to be saved as cookie
  }
 + PUT -- new account
  
  request {
    name : name,
    password : pw
  }
