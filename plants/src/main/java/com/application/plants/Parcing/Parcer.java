package com.application.plants.Parcing;

import com.application.plants.Parcing.Properties.*;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
//"D:/Проект/data/data"

@Service
public class Parcer {
//    public static String plantName = "Achillea millefolium";
//    public static Plant plant = new Plant();
    private URL resource = getClass().getClassLoader().getResource("data");
    public List<String> getNames(){
        List<String> names = new ArrayList<>();
        Path dataFolder;
        try {
            assert resource != null;
            dataFolder = Paths.get(resource.toURI());
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Invalid URI for data folder!", e);
        }
        try (Stream<Path> subFolders = Files.list(dataFolder)) { // Получаем список элементов в data
            subFolders.filter(Files::isDirectory)
                    .forEach(path -> names.add(path.getFileName().toString()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return names;
    }
    public void getInfo(Plant plant, String plantName, String propertyName) {
        Path dataFolder;
        System.out.println(resource);
        try {
            assert resource != null;
            dataFolder = Paths.get(resource.toURI());
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Invalid URI for data folder!", e);
        }
        try (Stream<Path> subFolders = Files.list(dataFolder)) { // Получаем список элементов в data
            Optional<Path> filteredPath = subFolders.filter(Files::isDirectory)
                    .filter(path -> path.getFileName().toString().equals(plantName))
                    .findFirst();
            if (filteredPath.isPresent()) {
                Path foundPath = filteredPath.get();
                processTxtFiles(foundPath, plant, propertyName);
//                changeComps(plant,foundPath);
            } else {
                System.out.println("Папка с именем '" + plantName + "' не найдена.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void processTxtFiles(Path folder, Plant plant, String propertyName) {
        try (Stream<Path> files = Files.list(folder)) { // Получаем список файлов в папке
            files.filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".txt"))
                    .filter(path -> path.toString().contains(propertyName))
                    .forEach(path -> {
                        try {
                            processPlant(propertyName, plant, path, folder);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }); // Выводим пути к файлам
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void processPlant(String propertyName, Plant plant, Path path, Path folder) throws IOException {
        List<Property> properties = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(Files.newInputStream(path)));
        String firstLine = reader.readLine();
        if (firstLine.equals("n/a")){
            plant.setStatus(0);
            return;
        }
        else{
            plant.setStatus(1);
        }
        String line;
        String regex = "^(.*?)\\t([\\d.]+)\\t([^\\t(]+)(.*)$";
        Pattern pattern = Pattern.compile(regex);
        while ((line = reader.readLine()) != null) {
            Matcher matcher = pattern.matcher(line);
            if (matcher.matches()) {
                String name = matcher.group(1).trim();
                String activationProb = matcher.group(2).trim();
                String component = matcher.group(3).trim();
                String otherProbs = matcher.group(4).trim();
                properties.add(new Property(name, activationProb, component, otherProbs));
            }
        }

        List<Property> newProperties = changeComps(plant,properties);
        switch (propertyName) {
            case "Antitargets":
                List<Antitargets> antitargets = new ArrayList<>();
                for (Property property : newProperties) {
                    antitargets.add(new Antitargets(property));
                }
                plant.setAntitargets(antitargets);
                break;
            case "Gene Expression Regulation":
                List<GeneExpression> geneExpressions = new ArrayList<>();
                for (Property property : newProperties) {
                    geneExpressions.add(new GeneExpression(property));
                }
                plant.setGeneExpressions(geneExpressions);
                break;
            case "Mechanisms of Action":
                List<MechActivation> mechActivations = new ArrayList<>();
                for (Property property : newProperties) {
                    mechActivations.add(new MechActivation(property));
                }
                plant.setMechActivations(mechActivations);
                break;
            case "Metabolism-Related Actions":
                List<Metabolism> metabolisms = new ArrayList<>();
                for (Property property : newProperties) {
                    metabolisms.add(new Metabolism(property));
                }
                plant.setMetabolisms(metabolisms);
                break;
            case "Pharmacological Effects":
                List<PharmEff> pharmEffs = new ArrayList<>();
                for (Property property : newProperties) {
                    pharmEffs.add(new PharmEff(property));
                }
                plant.setPharmEffs(pharmEffs);
                break;
            case "Predicted Activities":
                List<PredictedActivities> predictedActivities = new ArrayList<>();
                for (Property property : newProperties) {
                    predictedActivities.add(new PredictedActivities(property));
                }
                plant.setPredictedActivities(predictedActivities);
                break;
            case "Toxic and Adverse Effects":
                List<ToxicEff> toxicEffs = new ArrayList<>();
                for (Property property : newProperties) {
                    toxicEffs.add(new ToxicEff(property));
                }
                plant.setToxicEffs(toxicEffs);
                break;
            case "Transporters-Related Actions":
                List<Transporters> transporters = new ArrayList<>();
                for (Property property : newProperties) {
                    transporters.add(new Transporters(property));
                }
                plant.setTransporters(transporters);
                break;
        }
        reader.close();
    }

    public List<Property> changeComps(Plant plant, List<Property> properties){
        String folder = "data/" + plant.getName() + "/mol_info";
        ClassLoader classLoader = getClass().getClassLoader();
        URL resource = classLoader.getResource(folder);
//        System.out.println(folder);
//        System.out.println(resource);
        try {
            if (resource != null) {
                Path folderPath = Paths.get(resource.toURI());
                try (Stream<Path> files = Files.list(folderPath)) {
                    files.filter(Files::isRegularFile)
                            .filter(path -> path.toString().endsWith(".txt"))
                            .forEach(path -> {
                                try {
                                    fillNameComps(plant, path.getFileName().toString(), path, properties);
                                    changeCompsNames(plant,properties);
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            });
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return properties;
            }
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        return properties;
    }

    public static void changeCompsNames(Plant plant,List<Property> properties){
        Map<String,String> realCompsNames = plant.getRealCompsNames();
        for (Property property : properties) {
            String newCompound = property.getCompound();
            String newAnotherComps = property.getAnotherProbs();
                for (Map.Entry<String, String> entry : realCompsNames.entrySet()) {
                    if (newCompound.contains(entry.getKey())) {
                        newCompound = newCompound.replace(entry.getKey(), entry.getValue());
                    }
                    if (newAnotherComps.contains(entry.getKey())) {
                        newAnotherComps = newAnotherComps.replace(entry.getKey(), entry.getValue());
                    }
                }
                property.setCompound(newCompound);
            property.setAnotherProbs(newAnotherComps);
        }
    }

    public byte[] imageComp(String compName, Plant plant) throws IOException {
        Map<String,String> comps = plant.getRealCompsNames();
        String name = null;
        for (String compNamee : comps.keySet()){
            if (compName.equals(comps.get(compNamee))){
                name = compNamee;
            }
        }
        String folder = "data/" + plant.getName() + "/img";
        ClassLoader classLoader = getClass().getClassLoader();
        URL resource = classLoader.getResource(folder);
        Path folderPath = null;
        try {
            if (resource != null) {
                folderPath = Paths.get(resource.toURI());
                try (Stream<Path> files = Files.list(folderPath)) {
                    String finalName = name;
                    Optional<byte[]> result = files.filter(Files::isRegularFile)
                            .filter(path -> path.toString().endsWith(".png"))
                            .filter(path -> path.toString().contains(finalName))
                            .map(path -> {
                                try {
                                    return compImg(plant, compName, path);
                                } catch (IOException e) {
                                    throw new RuntimeException(e); // Преобразуем checked exception в runtime
                                }
                            })
                            .filter(bytes -> bytes != null && bytes.length > 0) // Фильтруем null и пустые массивы
                            .findFirst();
                    return result.orElse(null);
                }
            }
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public byte[] compImg(Plant plant, String compName, Path path) throws IOException {
        Map<String, String> compNames = plant.getRealCompsNames();
        byte[] bytes = Files.readAllBytes(path);
        for (Map.Entry<String,String> entry : compNames.entrySet()){
            if (path.toString().contains(entry.getKey()) && compName.equals(entry.getValue())){
                return bytes;
            }
        }
        return bytes;
    }

    public void fillNameComps(Plant plant, String comp, Path path, List<Property> properties) throws IOException {
        String compFileName = comp.substring(0,comp.indexOf('.'));
        BufferedReader reader = new BufferedReader(new InputStreamReader(Files.newInputStream(path)));
        String compName = reader.readLine();
        if (properties != null) {
            for (Property compound : properties) {
                if (compound.getCompound().contains(compFileName)) {
                    plant.getRealCompsNames().put(compFileName,compName);
                }
                if (compound.getAnotherProbs().contains(compFileName)) {
                    plant.getRealCompsNames().put(compFileName,compName);
                }
            }
        }
    }
}