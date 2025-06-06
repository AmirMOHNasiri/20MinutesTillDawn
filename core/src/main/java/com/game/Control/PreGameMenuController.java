package com.game.Control;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.game.Main;
import com.game.Model.Gun.Gun;
import com.game.Model.Gun.GunType;
import com.game.Model.Manage.GameAssetManager;
import com.game.Model.Player.Avatar;
import com.game.Model.Player.Character;
import com.game.View.GameView;
import com.game.View.PreGameMenuView;

public class PreGameMenuController {
    private PreGameMenuView view;

    public void setView(PreGameMenuView view) {
        this.view = view;
    }

    public void playButtonPressed() {
        if (view.getHeroButton().getText().toString().equals("Hero")
        || view.getGunButton().getText().toString().equals("Gun")
        || view.getTimeButton().getText().toString().equals("Time")) {
            view.getErrorLabel().setText("Please select all options");
            return;
        }
        //Character
        Character character = Character.getCharacter(view.getHeroButton().getText().toString());
        view.getCurrentPlayer().setMaxHp(character.getHp());
        view.getCurrentPlayer().setCurrentHp(character.getHp());
        view.getCurrentPlayer().setSpeed(character.getSpeed());
        view.getCurrentPlayer().setPlayerIdle(true);
        Animation<Texture> idleAnimation = GameAssetManager.getCharacterIdleAnimation(character.name());
        Animation<Texture> runingAnimation = GameAssetManager.getCharacterRunAnimation(character.name());
        Texture texture = GameAssetManager.getCharacterTexture(character.name());

        //Gun
        GunType type = GunType.getGunType(view.getGunButton().getText().toString());
        view.getCurrentPlayer().setGun(new Gun(type));

        //Time
        float time = Float.parseFloat(view.getTimeButton().getText().toString().replace("Min",""));

        view.getCurrentPlayer().setPosition(new Vector2(0,0));
        Main.getMain().setScreen(new GameView(new GameController(view.getCurrentPlayer(),time,texture,idleAnimation,runingAnimation)));
    }

    public void heroButtonPressed() {
        Skin skin = GameAssetManager.getGameAssetManager().getSkin();
        final Image dasher = new Image(Avatar.Dasher.getPortrait());
        final Label dasherHp = new Label("HP "+String.valueOf(Character.Dasher.getHp()), skin);
        final Label dasherSpeed = new Label("Speed "+String.valueOf(Character.Dasher.getSpeed()), skin);

        final Image diamond = new Image(Avatar.Diamond.getPortrait());
        final Label diamondHp = new Label("HP "+String.valueOf(Character.Diamond.getHp()), skin);
        final Label diamondSpeed = new Label("Speed "+String.valueOf(Character.Diamond.getSpeed()), skin);

        final Image lilith = new Image(Avatar.Lilith.getPortrait());
        final Label lilithHp = new Label("HP "+String.valueOf(Character.Lilith.getHp()), skin);
        final Label lilithSpeed = new Label("Speed "+String.valueOf(Character.Lilith.getSpeed()), skin);

        final Image scarlett = new Image(Avatar.Scarlett.getPortrait());
        final Label scarlettHp = new Label("HP "+String.valueOf(Character.Scarlett.getHp()), skin);
        final Label scarlettSpeed = new Label("Speed "+String.valueOf(Character.Scarlett.getSpeed()), skin);

        final Image shana = new Image(Avatar.Shana.getPortrait());
        final Label shanaHp = new Label("HP "+String.valueOf(Character.Shana.getHp()), skin);
        final Label shanaSpeed = new Label("Speed "+String.valueOf(Character.Shana.getSpeed()), skin);

        Dialog dialog = new Dialog("", skin);

        addCharacter(dialog,dasher,  "Dasher",  view.getHeroButton(),dasherHp,dasherSpeed);
        addCharacter(dialog,diamond, "Diamond", view.getHeroButton(),diamondHp,diamondSpeed);
        addCharacter(dialog,lilith,  "Lilith",  view.getHeroButton(),lilithHp,lilithSpeed);
        addCharacter(dialog,scarlett,"Scarlett",view.getHeroButton(),scarlettHp,scarlettSpeed);
        addCharacter(dialog,shana,   "Shana",   view.getHeroButton(),shanaHp,shanaSpeed);

        dialog.setModal(true);
        dialog.show(view.getStage());

        dialog.getColor().a = 0f;
        dialog.setTransform(true);
        dialog.setScale(0f);
        dialog.addAction(Actions.sequence(
            Actions.parallel(
                Actions.fadeIn(0.3f),
                Actions.scaleTo(1f,1f,0.3f, Interpolation.swingOut)
            )
        ));
    }

    public void GunButtonPressed(){
        Skin skin = GameAssetManager.getGameAssetManager().getSkin();
        final Image revolver = new Image(GunType.Revolver.getTexture());
        final Label revolverDmg = new Label("DMG "+String.valueOf(GunType.Revolver.getDamage()), skin);
        final Label revolverProjectile = new Label("Projectile "+String.valueOf(GunType.Revolver.getProjectile()), skin);
        final Label revolverReload = new Label("Reload "+String.valueOf(GunType.Revolver.getTimeReload())+"s", skin);
        final Label revolverAmmo = new Label("Ammo "+String.valueOf(GunType.Revolver.getAmmo()), skin);

        final Image shotgun = new Image(GunType.Shotgun.getTexture());
        final Label shotgunDmg = new Label("DMG "+String.valueOf(GunType.Shotgun.getDamage()), skin);
        final Label shotgunProjectile = new Label("Projectile "+String.valueOf(GunType.Shotgun.getProjectile()), skin);
        final Label shotgunReload = new Label("Reload "+String.valueOf(GunType.Shotgun.getTimeReload())+"s", skin);
        final Label shotgunAmmo = new Label("Ammo "+String.valueOf(GunType.Shotgun.getAmmo()), skin);

        final Image dualSmg = new Image(GunType.SMGsDual.getTexture());
        final Label dualSmgDmg = new Label("DMG "+String.valueOf(GunType.SMGsDual.getDamage()), skin);
        final Label dualSmgProjectile = new Label("Projectile "+String.valueOf(GunType.SMGsDual.getProjectile()), skin);
        final Label dualSmgReload = new Label("Reload "+String.valueOf(GunType.SMGsDual.getTimeReload())+"s", skin);
        final Label dualSmgAmmo = new Label("Ammo "+String.valueOf(GunType.SMGsDual.getAmmo()), skin);

        Dialog dialog = new Dialog("", skin);

        addCharacter(dialog,revolver,"Revolver",view.getGunButton(),revolverDmg,revolverProjectile,revolverReload,revolverAmmo);
        addCharacter(dialog,shotgun,"Shotgun",view.getGunButton(),shotgunDmg,shotgunProjectile,shotgunReload,shotgunAmmo);
        addCharacter(dialog,dualSmg,"SMGsDual",view.getGunButton(),dualSmgDmg,dualSmgProjectile,dualSmgReload,dualSmgAmmo);

        dialog.setModal(true);
        dialog.show(view.getStage());

        dialog.getColor().a = 0f;
        dialog.setTransform(true);
        dialog.setScale(0f);
        dialog.addAction(Actions.sequence(
            Actions.parallel(
                Actions.fadeIn(0.3f),
                Actions.scaleTo(1f,1f,0.3f, Interpolation.swingOut)
            )
        ));
    }

    public void timeButtonPressed(){
        Skin skin = GameAssetManager.getGameAssetManager().getSkin();

        Dialog dialog = new Dialog("", skin);

        final Label twoMin = new Label("2 Min", skin);
        twoMin.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                view.getTimeButton().setText(twoMin.getText().toString());

                dialog.addAction(Actions.sequence(
                    Actions.fadeOut(0.3f),
                    Actions.run(dialog::hide)
                ));
            }
        });
        final Label fiveMin = new Label("5 Min", skin );
        fiveMin.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                view.getTimeButton().setText(fiveMin.getText().toString());

                dialog.addAction(Actions.sequence(
                    Actions.fadeOut(0.3f),
                    Actions.run(dialog::hide)
                ));
            }
        });
        final Label tenMin = new Label("10 Min", skin);
        tenMin.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                view.getTimeButton().setText(tenMin.getText().toString());

                dialog.addAction(Actions.sequence(
                    Actions.fadeOut(0.3f),
                    Actions.run(dialog::hide)
                ));
            }
        });
        final Label twentyMin = new Label("20 Min", skin);
        twentyMin.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                view.getTimeButton().setText(twentyMin.getText().toString());

                dialog.addAction(Actions.sequence(
                    Actions.fadeOut(0.3f),
                    Actions.run(dialog::hide)
                ));
            }
        });

        dialog.getContentTable().add(twoMin).pad(5).row();
        dialog.getContentTable().add(fiveMin).pad(5).row();
        dialog.getContentTable().add(tenMin).pad(5).row();
        dialog.getContentTable().add(twentyMin).pad(5).row();

        dialog.setModal(true);
        dialog.show(view.getStage());

        dialog.getColor().a = 0f;
        dialog.setTransform(true);
        dialog.setScale(0f);
        dialog.addAction(Actions.sequence(
            Actions.parallel(
                Actions.fadeIn(0.3f),
                Actions.scaleTo(1f,1f,0.3f, Interpolation.swingOut)
            )
        ));
    }

    private void addCharacter(Dialog dialog, Image image, String name, TextButton button, Label... labels) {
        Table characterTable = new Table();
        Table character = new Table();
        Table characterDetails = new Table();

        image.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                button.setText(name);

                dialog.addAction(Actions.sequence(
                    Actions.fadeOut(0.3f),
                    Actions.run(dialog::hide)
                ));
            }
        });

        if (button == view.getHeroButton())
            character.add(image).width(image.getWidth()*0.5f).height(image.getHeight()*0.5f);
        else
            character.add(image).width(image.getWidth()*7.5f).height(image.getHeight()*7.5f);
        for (Label label : labels)
            characterDetails.add(label).row();
        characterTable.add(character);
        characterTable.add(characterDetails).padLeft(7.5f).padTop(5);

        dialog.getContentTable().add(characterTable).padBottom(10).row();
    }
}
