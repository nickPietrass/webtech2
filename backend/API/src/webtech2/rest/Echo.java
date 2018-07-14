package webtech2.rest;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

import webtech2.rest.auth.AuthRealm;

/**
 * Simple ping/pong test.
 * @author Ilja
 */
@ApplicationPath("/api")
@Path("")
public class Echo extends Application{
	
	@GET
    @Path("/ping")
    public Response plainPong(){
		return Response.ok("Pong!").build();
    }
	
	@POST
    @Path("/ping")
    public Response echoPong(String bodyText){
        return Response.ok("Pong: "+bodyText).build();
    }
	
	@GET
    @Path("/shiro")
    public Response shiroPong(){
		Subject currentUser = AuthRealm.getCurrentSubject();
		

		// let's login the current user so we can check against roles and permissions:
        if (!currentUser.isAuthenticated()) {
            UsernamePasswordToken token = new UsernamePasswordToken("lonestarr", "vespa");
            token.setRememberMe(true);
            try {
            	
                currentUser.login(token);
            } catch (UnknownAccountException uae) {
                System.out.println("There is no user with username of " + token.getPrincipal());
            } catch (IncorrectCredentialsException ice) {
                System.out.println("Password for account " + token.getPrincipal() + " was incorrect!");
            }
            // ... catch more exceptions here (maybe custom ones specific to your application?
            catch (AuthenticationException ae) {
                System.out.println("ERROR: "+ae.getMessage());
            }
        }
        
        
	    System.out.println("SHIRO: "+currentUser.getSession().getId());
	    
	    return Response.ok("OK: "+currentUser).build();
	}
}
