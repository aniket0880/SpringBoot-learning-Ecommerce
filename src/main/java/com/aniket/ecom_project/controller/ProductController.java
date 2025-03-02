package com.aniket.ecom_project.controller;

import com.aniket.ecom_project.model.Products;
import com.aniket.ecom_project.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


@RestController
@RequestMapping("/api")
@CrossOrigin
public class ProductController {

    @Autowired
    private ProductService service;


    @GetMapping("/products")
    public ResponseEntity<List<Products>> getAllProducts() {

        return new ResponseEntity<>(service.getAllProducts(), HttpStatus.OK);
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<Products> getProduct(@PathVariable int id) {
        Products product = service.getProduct(id);
        if (product != null) {
            return new ResponseEntity<>(product, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }


    }
    @PostMapping("/product")
    public ResponseEntity<?> addProduct(@RequestPart Products products,
                                        @RequestPart MultipartFile imageFile){

        try{
            Products products1=service.addProduct(products,imageFile);
            return new ResponseEntity<>(products1,HttpStatus.CREATED);
        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/product/{productId}/image")
    public ResponseEntity<byte[]> getImageByProductId(@PathVariable int productId){

        Products products=service.getProduct(productId);
        byte[] imageFile=products.getImageData();

        return ResponseEntity.ok().contentType(MediaType.valueOf(products.getImageType())).body(imageFile);
    }

    @PutMapping("/product/{id}")
    public ResponseEntity<String> updateProduct(@PathVariable int id ,
        @RequestPart Products products,
        @RequestPart MultipartFile imageFile)  {

        Products products1=null;
        try {

            products1 = service.updateProduct(id, products, imageFile);
        } catch(Exception e){
            return new ResponseEntity<>("Failed to update",HttpStatus.BAD_REQUEST);
        }
        if(products!=null)
            return new ResponseEntity<>("Updated",HttpStatus.OK);

        else{
            return new ResponseEntity<>("Failed to update",HttpStatus.BAD_REQUEST);
        }


    }
    @DeleteMapping("/product/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable int id){

        Products products=service.getProduct(id);
        if(products!=null){
            service.deleteProduct(id);
            return new ResponseEntity<>("Deleted",HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>("Product Not Found",HttpStatus.NOT_FOUND);
        }
    }
}