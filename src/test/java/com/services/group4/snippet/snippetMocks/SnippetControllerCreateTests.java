package com.services.group4.snippet.snippetMocks;

/*
@SpringBootTest
@AutoConfigureMockMvc
public class SnippetControllerCreateTests {

  @BeforeAll
  public static void setup() {
    DotenvConfig.loadEnv();
  }

  @Autowired private MockMvc mockMvc;

  @MockBean private PermissionService permissionService;

  @Test
  void testCreateSnippetForUser_Success() throws Exception {
    SnippetDto request = new SnippetDto("Test Snippet", "This is a test snippet.", "1.0", "Java");

    Mockito.when(permissionService.createOwnership(Mockito.anyLong(), Mockito.anyLong()))
        .thenReturn(new ResponseEntity<>(HttpStatus.CREATED));

    ObjectMapper objectMapper = new ObjectMapper();
    mockMvc
        .perform(
            post("/snippets/createByUser/{userId}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(content().string("Snippet created"));
  }
}

 */
