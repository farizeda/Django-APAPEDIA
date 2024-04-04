package apap.tk.apapedia.frontend.service;

import apap.tk.apapedia.frontend.dto.request.CreateCatalogueRequestDTO;
import apap.tk.apapedia.frontend.dto.response.CommonResponseDTO;
import apap.tk.apapedia.frontend.dto.rest.GetCatalogueDetailRestDTO;
import apap.tk.apapedia.frontend.dto.rest.GetCataloguesRestDTO;
import apap.tk.apapedia.frontend.dto.rest.GetCategoriesRestDTO;
import apap.tk.apapedia.frontend.model.User;
import apap.tk.apapedia.frontend.security.SecurityUserDetails;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;



@Service
public class CatalogueServiceImpl implements CatalogueService {
    @Value("${service.catalogue}")
    private String CATALOGUE_SERVICE_URL;

    private AmazonS3 s3client;
    @Value("${s3.endpointUrl}")
    private String endpointUrl;

    @Value("${s3.bucketName}")
    private String bucketName;

    @Value("${s3.accessKey}")
    private String accessKey;

    @Value("${s3.secretKey}")
    private String secretKey;

    private WebClient webClient;

    @PostConstruct
    public void initialize() {
        this.webClient = WebClient.builder().baseUrl(this.CATALOGUE_SERVICE_URL).build();

        AWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);
        this.s3client = new AmazonS3Client(credentials);
    }

    private File convertMultiPartToFile(MultipartFile file) {
        File convFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
        try (FileOutputStream fos = new FileOutputStream(convFile)) {
            fos.write(file.getBytes());
            return convFile;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    private String generateFileName(MultipartFile multiPart) {
        return new Date().getTime() + "-" + Objects.requireNonNull(multiPart.getOriginalFilename()).replace(" ", "_");
    }

    private void uploadFileTos3bucket(String fileName, File file) {
        s3client.putObject(new PutObjectRequest(bucketName, fileName, file)
                .withCannedAcl(CannedAccessControlList.PublicRead));
    }

    @Override
    public String uploadImage(MultipartFile multipartFile) throws IOException {
        File file = convertMultiPartToFile(multipartFile);
        if (file == null) {
            return null;
        }
        String fileName = generateFileName(multipartFile);
        String fileUrl = endpointUrl + "/" + fileName;
        uploadFileTos3bucket(fileName, file);
        boolean isDeleted = file.delete();
        if (isDeleted) {
            System.out.println("Local file is deleted!");
        }

        return fileUrl;
    }

    @Override
    public List<GetCategoriesRestDTO> getCategories() {
        return Objects.requireNonNull(this.webClient.get().uri("/category").retrieve().bodyToMono(new ParameterizedTypeReference<CommonResponseDTO<List<GetCategoriesRestDTO>>>() {
        }).block()).getContent();
    }

    @Override
    public CommonResponseDTO<?> postCatalogue(CreateCatalogueRequestDTO catalogueDTO, String imageUrl) {
        String token = ((SecurityUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getToken();

        Map<String, Object> data = new HashMap<>();
        data.put("name", catalogueDTO.getName());
        data.put("price", catalogueDTO.getPrice());
        data.put("description", catalogueDTO.getDescription());
        data.put("stock", catalogueDTO.getStock());
        data.put("imageUrl", imageUrl);
        data.put("categoryId", catalogueDTO.getCategoryId());
        return this.webClient.post().uri("/catalogue")                    .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON).bodyValue(data).retrieve().bodyToMono(CommonResponseDTO.class).block();
    }

    @Override
    public CommonResponseDTO<?> updateCatalogue(CreateCatalogueRequestDTO catalogueDTO, String imageUrl, UUID catalogueId) {
        String token = ((SecurityUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getToken();

        Map<String, Object> data = new HashMap<>();
        data.put("name", catalogueDTO.getName());
        data.put("price", catalogueDTO.getPrice());
        data.put("description", catalogueDTO.getDescription());
        data.put("stock", catalogueDTO.getStock());
        data.put("imageUrl", imageUrl);
        data.put("categoryId", catalogueDTO.getCategoryId());
        return this.webClient.put().uri(String.format("/catalogue/%s", catalogueId))
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(data).retrieve().bodyToMono(CommonResponseDTO.class).block();
    }

    @Override
    public CommonResponseDTO<List<GetCataloguesRestDTO>> getCatalogues(User user) {
        if (user == null) {
            return this.webClient.get()
                    .uri("/catalogue")
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<CommonResponseDTO<List<GetCataloguesRestDTO>>>() {})
                    .block();
        } else  {
           String token = ((SecurityUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getToken();

           return this.webClient.get()
                     .uri(String.format("/catalogue/seller/%s", user.getId()))
                    .header("Authorization", "Bearer " + token)
                     .retrieve()
                     .bodyToMono(new ParameterizedTypeReference<CommonResponseDTO<List<GetCataloguesRestDTO>>>() {})
                     .block();
        }
    }

    @Override
    public GetCatalogueDetailRestDTO getCatalogue(UUID catalogueId) {
        return Objects.requireNonNull(this.webClient.get()
                .uri(String.format("/catalogue/%s", catalogueId))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<CommonResponseDTO<GetCatalogueDetailRestDTO>>() {
                })
                .block()).getContent();
    }

    @Override
    public CommonResponseDTO<?> deleteCatalogue(UUID catalogueId) {
        String token = ((SecurityUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getToken();

        return webClient.delete()
                .uri(String.format("/catalogue/%s", catalogueId))
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .bodyToMono(CommonResponseDTO.class)
                .block();
    }
}
