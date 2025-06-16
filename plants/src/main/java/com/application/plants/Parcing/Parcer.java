package com.application.plants.Parcing;

import com.application.plants.Parcing.Properties.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;
//"D:/Проект/data/data"

@Service
public class Parcer {

    @Value("${data.folder}")
    private String dataFolderPath;
//    public static String plantName = "Achillea millefolium";
//    public static Plant plant = new Plant();

    public List<String> getNames(){
        List<String> names = new ArrayList<>();
        Path dataFolder = Paths.get(dataFolderPath);
        try (Stream<Path> subFolders = Files.list(dataFolder)) { // Получаем список элементов в data
            subFolders.filter(Files::isDirectory)
                    .forEach(path -> names.add(path.getFileName().toString()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return names;
    }
    public Optional<JsonNode> getInfo(Plant plant, String plantName, String propertyName) {
        Path dataFolder = Paths.get(dataFolderPath);
        try (Stream<Path> subFolders = Files.list(dataFolder)) {
            Optional<Path> filteredPath = subFolders
                    .filter(Files::isDirectory)
                    .filter(path -> path.getFileName().toString().equals(plantName))
                    .findFirst();

            if (filteredPath.isPresent()) {
                Path foundPath = filteredPath.get();
                Optional<JsonNode> json = processJsonFiles(foundPath, propertyName);
                if (json.isPresent()) {
                    return json; // если есть JSON — вернём
                } else {
                    processTxtFiles(foundPath, plant, propertyName); // иначе обрабатываем TXT
                }
            } else {
                System.out.println("Папка с именем '" + plantName + "' не найдена.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Optional.empty(); // ничего не нашли — пусть вернётся пусто
    }

    public Optional<JsonNode> processJsonFiles(Path folder, String propertyName) {
        ObjectMapper objectMapper = new ObjectMapper();

        try (Stream<Path> files = Files.list(folder)) {
            return files
                    .filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".json"))
                    .filter(path -> path.toString().contains(propertyName))
                    .findFirst()
                    .map(path -> {
                        try {
                            ArrayNode originalArray = (ArrayNode) objectMapper.readTree(path.toFile());
                            ArrayNode formattedArray = objectMapper.createArrayNode();

                            for (JsonNode item : originalArray) {
                                ObjectNode newItem = objectMapper.createObjectNode();
                                newItem.put("name", item.path("Name").asText());
                                newItem.put("activationProb", item.path("Activation_Prob").asText());
                                newItem.put("compound", item.path("Compound").asText());

                                ArrayNode anotherProbs = objectMapper.createArrayNode();
                                for (JsonNode prob : item.path("Another_Probs")) {
                                    ObjectNode newProb = objectMapper.createObjectNode();
                                    newProb.put("activation", prob.path("value").asText());
                                    newProb.put("name", prob.path("compound").asText());
                                    anotherProbs.add(newProb);
                                }

                                newItem.set("anotherProbsParcered", anotherProbs);
                                formattedArray.add(newItem);
                            }

                            ObjectNode root = objectMapper.createObjectNode();
                            root.set(propertyName, formattedArray); // <-- ключ берется из запроса
                            return root;

                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }
    private void processPlantJson(String propertyName, Plant plant, Path path, Path folder) {

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

    public List<Compound> parcAnotherProbs(String anotherProbs){
        Pattern pattern = Pattern.compile("(\\d\\.\\d{3})\\s(.+?)(\\).\\(|.$)");
        Matcher matcher = pattern.matcher(anotherProbs);
        List<Compound> compounds = new ArrayList<>();

        while (matcher.find()) {
            String value = matcher.group(1);
            String name = matcher.group(2);
            compounds.add(new Compound(value,name));
        }
        return compounds;
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
        for (Property property : newProperties) {
            property.setAnotherProbsParcered(parcAnotherProbs(property.getAnotherProbs()));
        }
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
        Path folderPath = Paths.get(dataFolderPath, plant.getName(), "mol_info");
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

    public byte[] imageComp(String compName, String plantName, Map<String,String> realCompsNames) throws IOException {
        String name = null;
        for (String comp : realCompsNames.keySet()){
            if (compName.equals(realCompsNames.get(comp))){
                name = comp;
            }
        }
        Path folderPath = Paths.get(dataFolderPath, plantName, "img");
            try (Stream<Path> files = Files.list(folderPath)) {
                String finalName = name;
                Optional<byte[]> result = files.filter(Files::isRegularFile)
                        .filter(path -> path.toString().endsWith(".png"))
                        .filter(path -> path.toString().contains(finalName))
                        .map(path -> {
                            try {
                                return compImg(realCompsNames, compName, path);
                            } catch (IOException e) {
                                throw new RuntimeException(e); // Преобразуем checked exception в runtime
                            }
                        })
                        .filter(bytes -> bytes != null && bytes.length > 0) // Фильтруем null и пустые массивы
                        .findFirst();
                return result.orElse(null);
            }
    }

    public byte[] compImg( Map<String, String> realCompsNames, String compName, Path path) throws IOException {
        byte[] bytes = Files.readAllBytes(path);
        for (Map.Entry<String,String> entry : realCompsNames.entrySet()){
            if (path.toString().contains(entry.getKey()) && compName.equals(entry.getValue())){
                return bytes;
            }
        }
        return bytes;
    }

    public Compound getCompoundWrapper(Map<String, String> realCompsNames, String plantName, String compName) throws IOException {
        String name = null;
        for (String comp : realCompsNames.keySet()){
            if (compName.equals(realCompsNames.get(comp))){
                name = comp;
            }
        }
        Path folderPath = Paths.get(dataFolderPath, plantName, "mol_info");
        try (Stream<Path> files = Files.list(folderPath)) {
            String finalName = name;
            Optional<Compound> result = files.filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".txt"))
                    .filter(path -> path.toString().contains(finalName))
                    .map(path -> {
                        try {
                            return getCompound(path);
                        } catch (IOException e) {
                            throw new RuntimeException(e); // Преобразуем checked exception в runtime
                        }
                    })
                    .findFirst();
            return result.orElse(null);
        }
    }
    public Compound getCompound(Path path) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(Files.newInputStream(path)));
        Compound compound = new Compound();
        compound.setName(reader.readLine());
        compound.setUIPACName(reader.readLine());
        compound.setCHEMBLId(reader.readLine());
        compound.setPubChemCID(reader.readLine());
        compound.setCHEBI(reader.readLine());
        compound.setMolecularFormula(reader.readLine());
        compound.setMolecularWeight(reader.readLine());
        compound.setStandardInChI(reader.readLine());
        compound.setStandardInChIKey(reader.readLine());
        compound.setSMILES(reader.readLine());
        return compound;
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