$(document).ready(function(){
	  /*BOUTTON*/
      $btn_register = $("#modal_register");
      $btn_connexion = $("#modal_connexion");
      /*CHAMPS*/
      $field_register_mail = $("#registerInputEmail");
      $field_register_username = $("#registerInputUsername");
      $field_register_password = $("#registerInputPassword");
      $field_register_confirmPass = $("#confirmRegisterInputPassword");
      $field_connexion_username = $("#connexionInputUsername");
      $field_connexion_password = $("#connexionInputPassword")
      /*ALERT*/
      $alert_register = $("#alert_register");
      $alert_register_message = $("#alert_register_message");
      $alert_connexion = $("#alert_connexion");
      $alert_connexion_message = $("#alert_connexion_message");

      /*EVENT MODAL REGISTER*/
      $field_register_mail.keyup(showModalRegisterSubmit);
      $field_register_username.keyup(showModalRegisterSubmit);
      $field_register_password.keyup(showModalRegisterSubmit);
      $field_register_confirmPass.keyup(showModalRegisterSubmit);
      /*EVENT MODAL CONNEXION*/
      $field_connexion_username.keyup(showModalConnexionSubmit);
      $field_connexion_password.keyup(showModalConnexionSubmit);
      /*OTHER*/
      $div_img = $("#divImg");
      $div_toaster =$("#divToast");
      
      /*VAR*/
      var emptyArr = [];
      emptyArr.push("alert-info");
      emptyArr.unshift("alert-info");

      /*BTN DISABLED BY DEFAULT*/
      $btn_register.attr("disabled", true);
      $btn_connexion.attr("disabled", true);
      
      $('.toast').toast('show');
      setImage();
      setToast();

      function showModalRegisterSubmit(){
       if (!isEmptyField($field_register_mail) && !isEmptyField($field_register_username) && !isEmptyField($field_register_password) && !isEmptyField($field_register_confirmPass) && isValidField($field_register_mail)) {
       	if ($field_register_password.val() === $field_register_confirmPass.val()) {
       		setMessageAndState($alert_register,$alert_register_message,getAlert(0),"Vous pouvez appuyer sur le bouton pour vous enregistrer");
       		//loading animation set visisibility to show
       		$btn_register.attr("disabled", false);
       	}else{
       		setMessageAndState($alert_register,$alert_register_message,getAlert(3),"les mots de passes ne sont pas identiques");
       		$btn_register.attr("disabled", true);
       	}
       }else{
       	setMessageAndState($alert_register,$alert_register_message,getAlert(1),"Tous les champs doivent êtres remplis et l'adresse email doit être valide");
       	$btn_register.attr("disabled", true);
       }
      }

      function showModalConnexionSubmit(){
      	if (!isEmptyField($field_connexion_username) && !isEmptyField($field_connexion_password)){
      		setMessageAndState($alert_connexion,$alert_connexion_message,getAlert(0),"Vous pouvez appuyer sur le bouton pour vous connecter");
      		//loading animation set visisibility to show
      		$btn_connexion.attr("disabled", false);
      	}else{
      		setMessageAndState($alert_connexion,$alert_connexion_message,getAlert(1),"Tous les champs doivent êtres remplis");
          	$btn_connexion.attr("disabled", true);
      	}
      }

      function isEmptyField(obj){
      	if (obj.val() === ''){
     		return true;
		}
		return false;
      }
      
      function isValidField(obj){
    	  var regex = /^[A-Z0-9._%+-]+@([A-Z0-9-]+\.)+[A-Z]{2,4}$/i;
    	  if(regex.test(obj.val())){
    		  return true
    	  }
    	  return false
      }

      function setMessageAndState(objParent,objFils,state,message){//rajouter un tableau pour garder la derniere valeur [0]modal creation [1]modal register
      	if ($alert_register.attr('id')=="alert_register") {
      		objParent.toggleClass(emptyArr[1]+" "+state);
      		emptyArr.pop();
      		emptyArr.push(state);
      	}else{
      		objParent.toggleClass(emptyArr[0]+" "+state);
      		emptyArr.shift();
      		emptyArr.unshift(state);
      	}
      	objFils.empty();
      	objFils.html(message);
      }

      function getAlert(state){
      	var str ="";
      	switch(state) {
      		case 0:
    		str = "alert-success";
   			break;
  			case 1:
    		str = "alert-info";
    		break;
    		case 2:
    		str = "alert-warning";
   			break;
  			case 3:
    		str = "alert-danger";
    		break;
		} 
		return str;
      }

      $btn_register.click(function(){
      	$.getJSON('Register', {
      	    registerInputEmail : $field_register_mail.val(),
      	    registerInputUsername : $field_register_username.val(),
      	    registerInputPassword : $field_register_password.val()
      	  })
      	    .done(function(data) {
      	    //loading animation set visisibility to hidden
      	      console.log("Statut : "+data.statut);
      	      console.log("Msg : "+data.msg);
      	      if(data.statut == "SUCCESS"){
      	    	setMessageAndState($alert_register,$alert_register_message,getAlert(0),data.msg);
      	    	location.href = "http://localhost:8080/test01/HomeUser"  
      	      }else{
      	    	setMessageAndState($alert_register,$alert_register_message,getAlert(3),data.msg);
      	      }
      	    });
      });

      $btn_connexion.click(function(){
    	  $.getJSON('Login', {
    		  connexionInputUsername : $field_connexion_username.val(),
    		  connexionInputPassword : $field_connexion_password.val()
        	  })
        	    .done(function(data) {
        	    //loading animation set visisibility to hidden
        	      console.log("Statut : "+data.statut);
        	      console.log("Msg : "+data.msg);
        	      if(data.statut == "SUCCESS"){
        	    	setMessageAndState($alert_connexion,$alert_connexion_message,getAlert(0),data.msg);
        	    	location.href = "http://localhost:8080/test01/HomeUser"  
        	      }else{
        	    	setMessageAndState($alert_connexion,$alert_connexion_message,getAlert(3),data.msg);
        	      }
        	    });
      });
      function setImage(){
          $.getJSON('https://api.flickr.com/services/feeds/photos_public.gne?jsoncallback=?', {
                  tags: "Louvre",
                  tagmode: "any",
                  format: "json"
                })
                  .done(function(data) {
                     $.each( data.items, function( i, item ){
                        creationCard(item.media.m);
                        if(i === 11){
                          return false;
                        }
                     });
              });
        }

        function creationCard(src){
          $divGlobal = $("<div>").attr("class","col-md-4");
          $divsub = $("<div>").attr("class","card mb-6 shadow-sm");

          $divGlobal.appendTo($div_img);
          $divsub.appendTo($divGlobal);

          $( "<img>" ).attr( {
                "src": src,
                "class": "card-img-top"
            } ).appendTo($divsub);
        }
        
        function creationToast(title,date,content){
        	/*DECLARATION*/
            $divPrincipal =  $("<div>").attr({
                "role": "alert",
                "class": "toast",
                "aria-live" : "assertive",
                "aria-atomic" : "true",
                "data-autohide" : "false"
            } );
            $divToastHeader =  $("<div>").attr("class","toast-header");
            
            $strongToast = $("<strong>").attr("class","mr-auto");
            $strongToast.html(title);
            
            $smallToast = $("<small>").html(date);
            
            $btnToast = $("<button>").attr({
                "type": "button",
                "class": "ml-2 mb-1 close",
                "data-dismiss" : "toast",
                "aria-label" : "Close"
            } );
            
            $spanToast = $("<span>").attr("aria-hidden","true");
            $spanToast.html("&times;");
            
            $divToastBody = $("<div>").attr("class","toast-body");
            $divToastBody.html(content);
            
            /*APPEND*/
            $spanToast.appendTo($btnToast);
            
            $strongToast.appendTo($divToastHeader);
            $smallToast.appendTo($divToastHeader);
            $btnToast.appendTo($divToastHeader);
            
            $divToastHeader.appendTo($divPrincipal);
            $divToastBody.appendTo($divPrincipal);
            
            $divPrincipal.appendTo($div_toaster);
            
            $divPrincipal.toast('show');
          }
        
        function setToast(){
            $.getJSON('AlertJson')
                    .done(function(data) {
                       $.each( data.rows, function( i, item ){
                    	   creationToast(item.Alert_title,item.Alert_date,item.Alert_content);
                       });
                });
          }
});