package br.com.sdum.interceptor;

import java.util.Arrays;
import java.util.Collection;

import javax.inject.Inject;

import br.com.caelum.vraptor.InterceptionException;
import br.com.caelum.vraptor.Intercepts;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.core.InterceptorStack;
import br.com.caelum.vraptor.interceptor.Interceptor;
import br.com.caelum.vraptor.resource.ResourceMethod;
import br.com.sdum.annotation.Permissao;
import br.com.sdum.controller.LoginController;
import br.com.sdum.enums.Roles;
import br.com.sdum.session.UsuarioSession;

@Intercepts
public class AuthorizationInterceptor implements Interceptor {

	@Inject	
	private UsuarioSession usuarioSession;

	@Inject
	private Result result;

	public AuthorizationInterceptor() { super(); }

	public void intercept(InterceptorStack interceptorStack, ResourceMethod resourceMethod, Object object) throws InterceptionException {
		
		Permissao permissao = resourceMethod.getMethod().getAnnotation(Permissao.class);
		Collection<Roles> collections = Arrays.asList(permissao.value());
		
		try{
			if(collections.contains(Roles.OFF)){
				if(usuarioSession.getUsuario() != null){
					result.redirectTo(LoginController.class).formulario();
				}else{
					interceptorStack.next(resourceMethod, object);
				}	
			}else{
				if(usuarioSession.getUsuario() != null){
					interceptorStack.next(resourceMethod, object);
				}else{
					result.redirectTo(LoginController.class).formulario();
				}
			}	
		}catch(NullPointerException n){
			result.redirectTo(LoginController.class).formulario();	
		}	
	}

	public boolean accepts(ResourceMethod resourceMethod) {
		return resourceMethod.containsAnnotation(Permissao.class);	
	}
}