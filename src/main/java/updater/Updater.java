package updater;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.persistence.NoResultException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import persistence.entity.Ingredient;
import persistence.entity.RcpPart;
import persistence.entity.Recipe;
import util.BuilderUtil;
import util.JsonUtil;
import util.NameFormat;
import util.Request;


/**
 * Updater
 * DB 업데이트용 클래스
 */

public class Updater implements Runnable{
    public Updater() {

    }

    @Override
    public void run() {
        int start = 1001;
        int end = 2000;
        // TODO 리팩터링
        String reqUrl = "http://openapi.foodsafetykorea.go.kr/api/891ac565f2f34c359279/COOKRCP01/json/"+start+"/"+end;
        String jsonStr = Request.httpRequest(reqUrl);
//         String fileName = "sample/sample1000.json";
//         String jsonStr = readSample(fileName);

        JSONObject jsonObj = JsonUtil.convertJsonObj(jsonStr);
        JSONArray recipeJsons = (JSONArray) ((JSONObject)jsonObj.get("COOKRCP01")).get("row");
        update(recipeJsons);
    }

    private void update(JSONArray recipeJsons) {
        CustomConnectionDAO ccdao = new CustomConnectionDAO();
        ccdao.initConnection();

        for (Object recipeJson : recipeJsons) {
            Recipe.RecipeBuilder rb = Recipe.builder();
            List<Ingredient> ingredients = new ArrayList<>();
            convertEntity((JSONObject) recipeJson, rb, ingredients);
            Recipe recipe = rb.build();

            // Check Duplication and Execute Query
            if (!isDuplication(ccdao, recipe)) {
                execUpdateQuery(ccdao, recipe, ingredients);
            }
        }
        ccdao.closeConnection();
    }

    private void convertEntity(JSONObject recipeJson, Recipe.RecipeBuilder rb, List<Ingredient> ingredients) {
        // Data Parsing
        Set<String> keySet = recipeJson.keySet();
        for (String key : keySet) {
            String data = (String) recipeJson.get(key);
            if (key.contains("SEQ")) {
                rb.rcpSeq(Long.parseLong(data));
            }
            else if (key.contains("INFO")) {
                String setterName = NameFormat.convert2CamelCase(key);
                Float value = Float.parseFloat(("0" + data).replaceAll("[^0-9.]", ""));
                BuilderUtil.dataBinding(rb, setterName, value);
            }
            else if (key.equals("RCP_PARTS_DTLS")) {
                ingredientsParsing(data, ingredients);
            }
        }
        rb.rcpJson(recipeJson.toJSONString());
    }

    public void ingredientsParsing(String data, List<Ingredient> ingredients) {
        if (data.contains("[ 2인분 ] 토마토(2개), ")) {
            System.out.println();
        }

        String unitAmount = "[0-9½↉⅓⅔¼¾⅕⅖⅗⅘⅙⅚⅐⅛][0-9./~xX]*";
        String specialChars = "[\\(\\)\\[\\]\'\"-<>]";
        String[] units = {"g", "ml", "mg", "cm", "장", "개", "가지", "cc", "인치"};

        String[] lines = data.split("[\n:]");
        Pattern[] patterns = new Pattern[units.length];
        String name;

        for (int i=0; i<units.length; i++) {
            patterns[i] = Pattern.compile(unitAmount + units[i]);
        }

        for (String line : lines) {
            line = line.replaceAll(" ", "");
            String[] igdStrs = line.split(",");

            for (String igdStr : igdStrs) {
                for (int i=0; i<units.length; i++) {
                    Matcher m = patterns[i].matcher(igdStr);

                    if (m.find()) {
                        for (String unit : units) {
                            igdStr = igdStr.replaceAll(unitAmount+unit, "");
                        }

                        name = igdStr
                                .replaceAll("<.*>", "")
                                .replaceAll("\\{.*\\}", "")
                                .replaceAll("\\(.*\\)", "")
                                .replaceAll("\\[.*\\]", "")
                                .replaceAll(specialChars, "");
                        if (!name.equals("")) {
                            ingredients.add(Ingredient.builder().name(name).build());
                            break;
                        }
                    }
                }
            }
        }
    }

    private boolean isDuplication(CustomConnectionDAO ccdao, Recipe recipe) {
        return (Boolean) ccdao.execQuery(em -> {
            return 0 < em.createQuery("SELECT r FROM Recipe AS r WHERE r.rcpSeq=:rcp_seq", Recipe.class)
                    .setParameter("rcp_seq", recipe.getRcpSeq())
                    .getResultList().size();
        });
    }

    private void execUpdateQuery(CustomConnectionDAO ccdao, Recipe recipe, List<Ingredient> ingredients) {
        ccdao.execQuery(em -> {
            em.persist(recipe);
            return null;
        });
        ccdao.execQuery(em -> {
            for (int i=0; i<ingredients.size(); i++) {
                Ingredient cur = ingredients.get(i);
                try {
                    cur = em.createQuery("SELECT i FROM Ingredient AS i WHERE i.name=:name", Ingredient.class)
                            .setParameter("name", cur.getName())
                            .getSingleResult();
                    ingredients.set(i, cur);
                }
                catch (NoResultException nre) {
                    em.persist(cur);
                }
            }
            return null;
        });
        ccdao.execQuery(em -> {
            for (Ingredient ingredient : ingredients) {
                em.persist(
                        RcpPart.builder()
                                .recipe(recipe)
                                .ingredient(ingredient)
                                .build()
                );
            }
            return null;
        });
    }


    public String readSample(String fileName) {
        StringBuilder sb = new StringBuilder();
        FileReader fr = null;
        try {
            fr = new FileReader(fileName);

            int readCharNo;
            char[] buff = new char[100];

            while ((readCharNo=fr.read(buff)) != -1) {
                String ns = new String(buff, 0, readCharNo);
                sb.append(ns);
            }
            fr.close();
        } catch (FileNotFoundException e) {
            System.out.println("파일 못찾음");
        } catch (IOException e) {
            System.out.println("io except");
        }

        return sb.toString();
    }
}
