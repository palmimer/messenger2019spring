package com.progmatic.messenger2019spring.controller;

import com.progmatic.messenger2019spring.UserStatistics;
import com.progmatic.messenger2019spring.domain.Message;
import com.progmatic.messenger2019spring.service.MessageServiceImpl;
import java.util.ArrayList;
import java.util.List;
import org.hamcrest.Matchers;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import org.mockito.MockitoAnnotations;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

/**
 *
 * @author Varga János
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class MessageControllerTest {

    MockMvc mockMvc;

    MessageServiceImpl messageService;
    @Mock
    UserStatistics userStatistics;

    public MessageControllerTest() {
    }

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        messageService = Mockito.mock(MessageServiceImpl.class); //Mockolunk egy MessageServiceImpl-t. A @Mock annotacioval is lehetne

        List<Message> messagesToReturn = new ArrayList<>(); //Letrehozzuk a teszt listankat
        messagesToReturn.add(new Message("Test Message1", "Test1"));
        messagesToReturn.add(new Message("Test Message2", "Test2"));
        //messagesToReturn.add(new Message("Test3", "Test Message3"));
        Mockito.when(
                messageService.filterMessages(Mockito.anyString(),
                        Mockito.anyString(),
                        Mockito.any(),
                        Mockito.any(),
                        Mockito.anyString(),
                        Mockito.anyInt(),
                        Mockito.anyBoolean(),
                        Mockito.anyBoolean()))
                .thenReturn(messagesToReturn); //A messageService filterMessages metodus hivasa utan (tetszoleges parameterekkel) mindig a teszt listankkal ter vissza.

        mockMvc = MockMvcBuilders
                .standaloneSetup(new MessageController(messageService, userStatistics)) //Letrehozzuk az egy MesageController-bol allo kornyezetunket. A fuggosegeit pedig mockoltuk
                .setViewResolvers(new InternalResourceViewResolver("", ".html")) //Kell egy ViewResolver, mert az alap nem jo, a thymeleaf-es pedig ilyenkor nem mukodik
                .build();
    }

    @Test
    public void testListMessages() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .get("/messages")) //Inditunk egy get request-et a /messages cimre
                .andExpect(MockMvcResultMatchers.view().name("messages")); //A controller altal visszaadott view-neve "messages" kell legyen. Ezt varjuk el
    }

    @Test
    public void testListMessages2() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .get("/messages"))
                .andExpect(MockMvcResultMatchers.status().isOk()) //A visszaadott statusz kod 200 OK kell legyen
                .andExpect(MockMvcResultMatchers.model().attributeExists("messages")) //A model-be be kellett kerulnie egy messages atributumnak
                .andExpect(MockMvcResultMatchers.model().attribute("messages", Matchers.hasSize(2))) //A messages kulcs-hoz egy Message lista fog tartozni, aminek a merete 2
                .andExpect(MockMvcResultMatchers.model().attribute("messages", Matchers.hasItem( //Ebben a listaban van egy olyan elem, amire az allabbiak mind igazak:
                        Matchers.allOf(
                                Matchers.hasProperty("author", Matchers.is("Test1")), //Az author field-jenek erteke Test1
                                Matchers.hasProperty("text", Matchers.is("Test Message1")) //A text field-jenek erteke Test Message1
                        )
                )));

        Mockito.verify(messageService, Mockito.times(1)).filterMessages(Mockito.anyString(), //A messageService filterMessages metodusat pontosan 1x hivtak meg a teszt kozben, barmilyen parameterekkel
                Mockito.anyString(),
                Mockito.any(),
                Mockito.any(),
                Mockito.anyString(),
                Mockito.anyInt(),
                Mockito.anyBoolean(),
                Mockito.anyBoolean());

        Mockito.verifyNoMoreInteractions(messageService); //Es ezen kivul nem hivtak mas metodust a messageService-en

//        List<Message> returnedMessages = (List<Message>) result.getModelAndView()
//                .getModel().get("messages");
//        
//        assertEquals(2, returnedMessages.size());
    }

    @Test
    public void testOneMessage() throws Exception {
        Mockito.when(messageService.getMessageById(0)).thenReturn(new Message("0. Üzenet", "User"));

        mockMvc.perform(MockMvcRequestBuilders.get("/messages/0"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("oneMessage"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("message"))
                .andExpect(MockMvcResultMatchers.model().attribute("message", Matchers.allOf(
                        Matchers.hasProperty("author", Matchers.is("User")),
                        Matchers.hasProperty("text", Matchers.is("0. Üzenet"))
                )));

        Mockito.verify(messageService, Mockito.times(1)).getMessageById(0);
        Mockito.verifyNoMoreInteractions(messageService);
    }

    @Test
    public void testDeleteMessage() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/messages/delete/0"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/messages"));
        
        Mockito.verify(messageService, Mockito.times(1)).deleteMessage(0);
        Mockito.verifyNoMoreInteractions(messageService);
    }

    @Test
    public void testShowCreateMessage() throws Exception {
        Mockito.when(messageService.getMessageById(0)).thenReturn(new Message("0. Üzenet", "User"));

        mockMvc.perform(MockMvcRequestBuilders.get("/messages/create"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("newMessage"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("message"))
                .andExpect(MockMvcResultMatchers.model().attribute("message", Matchers.hasProperty("text", Matchers.is(""))));

    }

    @Test
    @WithMockUser(roles = "USER", username = "Sanyi") //Ezt a tesztet egy Sanyi nevu felhasznaloval futtatjuk, akinek van USER role-ja.
    public void testCreateMessage() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .post("/messages/create")
                .param("text", "Ez egy szöveg")) //Egy post request-et kuldunk, ahol megadjuk az uzenet szoveget.
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/messages"));

        ArgumentCaptor<Message> messageParam = ArgumentCaptor.forClass(Message.class); //Ez az objektum fogja majd elkapni a MessageController altal a messageService addNewMessage metodusanak atadott uj Message objektumot
        Mockito.verify(messageService, times(1)).addNewMessage(messageParam.capture()); //itt
        verifyNoMoreInteractions(messageService);

        assertEquals("Sanyi", messageParam.getValue().getAuthor()); //Megnezzuk, hogy az elkeszult (elkapott) Message objektumnak az author field-je tenyleg Sanyi lett-e (ez volt a felhasznalo neve)
        assertEquals("Ez egy szöveg", messageParam.getValue().getText()); //Az uzenet pedig tenyleg Ez egy szoveg-e (ami a post request-ben volt)

    }

    @Test
    @WithMockUser(roles = "USER", username = "Sanyi")
    public void testCreateMessageValidation() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .post("/messages/create")
                .param("text", "Rövid"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("newMessage"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("message"))
                .andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("message", "text"));

        Mockito.verify(messageService, Mockito.never()).addNewMessage(Mockito.any());
        verifyNoMoreInteractions(messageService);
    }

}
