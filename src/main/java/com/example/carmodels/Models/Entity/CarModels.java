package com.example.carmodels.Models.Entity;

import com.example.carmodels.Models.Base.BaseModel;
import jakarta.persistence.*;

@Entity
public class CarModels extends BaseModel {
    @Column
    String manufacturer;
    @Column
    String carModel;
    @Column
    int carYear;
    @Column(nullable = true, length = 64)
    String photos;

    public void setPhotosImagePath(String photosImagePath) {
        PhotosImagePath = photosImagePath;
    }

    String PhotosImagePath;
    @Transient
    public String getPhotosImagePath() {
        if (photos == null || false) {
            return null;
        }

        return "/user-photos/" + this.getId() + "/" + photos;
    }

    public CarModels() {
        super();
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getCarModel() {
        return carModel;
    }

    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }

    public int getCarYear() {
        return carYear;
    }

    public void setCarYear(int carYear) {
        this.carYear = carYear;
    }

    public String getPhotos() {
        return photos;
    }

    public void setPhotos(String photos) {
        this.photos = photos;
    }


}
