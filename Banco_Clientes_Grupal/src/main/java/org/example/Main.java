package org.example;

import org.example.clientes.mappers.UsuarioMapper;
import org.example.clientes.model.Usuario;
import org.example.rest.RetrofitClient;
import org.example.rest.UserApiRest;
import org.example.rest.repository.UserRemoteRepository;
import org.example.rest.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {

        /* TODO ESTO SE DEBERÁ USAR EN EL MAIN PERO AL LLAMAR AL REPOSITORIO EN REALIDAD DEBE LLAMAR AL SERVICIO, MODIFICAR CUANDO HAGAMOS EL MAIN

        System.out.println("Systema de obtención de la lista en Tiempo Real");
        repository.getAllAsFlux().subscribe(
                lista -> {
                    System.out.println("👉 Lista de Funkos actulizada: " + lista);
                },
                error -> System.err.println("Se ha producido un error: " + error),
                () -> System.out.println("Completado")
        );

        System.out.println("Sistema de obtención de notificaciones en Tiempo Real");
        repository.getNotificationAsFlux().subscribe(
                notificacion -> System.out.println("🟢 Notificación: " + notificacion),
                error -> System.err.println("Se ha producido un error: " + error),
                () -> System.out.println("Completado")
        );
        */

        System.out.println("Hello world!");
        logger.debug("userApiRest: " + UserApiRest.API_USERS_URL);

        var retrofit = RetrofitClient.getClient(UserApiRest.API_USERS_URL);
        var userApiRest = retrofit.create(UserApiRest.class);
        UserRemoteRepository userRemoteRepository = new UserRemoteRepository(userApiRest);
        UserService userService = new UserService(userRemoteRepository);


        System.out.println(".............................");
        System.out.println("Recuperando todos los usuarios de la API-REST");
        var usuarios = userService.getAllAsync();
        usuarios
                .peek(lista->lista.forEach(System.out::println))
                .peekLeft(error-> System.out.println("Error recuperando todos los usuarios"+ error.getMessage()));

        System.out.println(".............................");
        System.out.println("Recuperando Usuario existente de la API-REST");
        int id = 1;
        var usuario = userService.getByIdAsync(id);
        usuario
                .peek(user-> {System.out.println("Usuario " + id +" Encontrado " + user);})
                .peekLeft(error-> {System.out.println("Error: " + error.getCode() + " - " + error.getMessage());});

        /*System.out.println(".............................");
        System.out.println("Recuperando Usuario NO existente de la API-REST");
        int id2 = 100;
        userService.getByIdAsync(id2)
                .peek(user-> {System.out.println("Usuario " + id2 +" No Encontrado " + user);})
                .peekLeft(error -> {System.out.println("Error: "+ error.getCode() +": " + error.getMessage());});
*/
        System.out.println(".............................");
        System.out.println("Creando Usuario en la API-REST");
        Usuario user =  Usuario.builder()
                .id(2312312312312132123L)
                .nombre("Prueba")
                .userName("UsuarioPrueba")
                .email("usuarioprueba@mail.com")
                .build();
        userService.createUserAsync(user)
                .peek(userCreated-> { System.out.println("Usuario creado correctamente: " + userCreated);})
                .peekLeft(error-> {System.out.println("Error: "+ error.getCode() +": " + error.getMessage());});

        System.out.println(".............................");
        System.out.println("Eliminando Usuario 1 en la API-REST");
        userService.deleteUserAsync(1)
                .peek(userDeleted-> { System.out.println("Usuario " + id + " eliminado correctamente");})
                .peekLeft(error-> {System.out.println("Error: "+ error.getCode() +": " + error.getMessage());});

        System.out.println(".............................");
        System.out.println("Actualizando Usuario 4 en la API-REST");
        Usuario updateUser = Usuario.builder()
                .id(4L)
                .nombre("Nombre actualizado")
                .userName("Username actualizado")
                .email("emailActualizado@mail.com")
                .build();
        userService.updateUserAsync( (Integer.parseInt(updateUser.getId().toString())), updateUser)
                .peek(userUpdated-> { System.out.println("Usuario "+ userUpdated.getId() +" Actualizado: " + userUpdated);})
                .peekLeft(error-> {System.out.println("Error: "+ error.getCode() +": " + error.getMessage());});

        System.out.println(".............................");
        System.out.println("Actualizando Usuario NO existente 2124 en la API-REST");
        Usuario updateUser2 = Usuario.builder()
                .id(2124L)
                .nombre("Nombre actualizado")
                .userName("Username actualizado")
                .email("emailActualizado@mail.com")
                .build();

        userService.updateUserAsync( (Integer.parseInt(updateUser2.getId().toString())), updateUser)
                .peek(userUpdated-> { System.out.println("Usuario "+ userUpdated.getId() +" Actualizado: " + userUpdated);})
                .peekLeft(error-> {System.out.println("Error: "+ error.getCode() +": " + error.getMessage());});

        System.out.println(".............................");
        System.out.println("Eliminando Usuario 1 en la API-REST");
        userService.deleteUserAsync(1)
                .peek(userDeleted-> { System.out.println("Usuario 1 eliminado correctamente");})
                .peekLeft(error-> {System.out.println("Error: "+ error.getCode() +": " + error.getMessage());});

        System.out.println(".............................");
        System.out.println("Eliminando Usuario inexistente en la API-REST");
        userService.deleteUserAsync(100)
                .peek(userDeleted-> { System.out.println("Usuario 100 eliminado correctamente");})
                .peekLeft(error-> {System.out.println("Error: "+ error.getCode() +": " + error.getMessage());});

    System.exit(0);
    }
}