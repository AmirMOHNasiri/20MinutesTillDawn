package com.game.Control;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.game.Main;
import com.game.Model.Player.Avatar;
import com.game.Model.Player.Player;
import com.game.View.MainMenuView;
import com.game.View.ProfileMenuView;
import com.game.View.RegisterMenuView;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ProfileMenuController {
    private ProfileMenuView view;
    private Player currentPlayer;
    private Skin skin;

    public ProfileMenuController(Skin skin, Player currentPlayer) {
        this.skin = skin;
        this.currentPlayer = currentPlayer;
    }

    public void setView(ProfileMenuView view) {
        this.view = view;
    }

    public void handleChangeUsername() {
        final TextField newUsernameField = new TextField("", skin);
        newUsernameField.setMessageText("Enter new username");

        final Label errorLabel = new Label("", skin);
        errorLabel.setColor(Color.RED);

        Dialog dialog = new Dialog("", skin) {
            @Override
            protected void result(Object object) {
                if ((Boolean) object) {
                    String newUsername = newUsernameField.getText();

                    if (newUsername.isEmpty()) {
                        errorLabel.setText("Please fill all fields.");
                        shakeDialog(this);
                        cancel();
                        return;
                    }
                    if (Player.getPlayerByUsername(newUsername) != null) {
                        errorLabel.setText("Username is already taken.");
                        shakeDialog(this);
                        cancel();
                        return;
                    }

                    currentPlayer.setUsername(newUsername);
                    currentPlayer.save();
                    view.showSuccessMessage("Username changed successfully!");
                    view.updateAvatarDisplay();
                }
                addAction(Actions.sequence(Actions.fadeOut(0.3f), Actions.run(this::remove)));
            }
        };

        Table content = dialog.getContentTable();
        content.add(new Label("New Username:", skin)).padTop(10).row();
        content.add(newUsernameField).width(400).padBottom(10).row();
        content.add(errorLabel).padTop(10).row();

        dialog.button("Ok", true).button("Cancel", false);
        showAnimatedDialog(dialog);
    }

    public void handleChangePassword() {
        final TextField newPasswordField = new TextField("", skin);
        newPasswordField.setMessageText("Enter new password");

        final TextField confirmPasswordField = new TextField("", skin);
        confirmPasswordField.setMessageText("Confirm new password");

        final Label errLabel = new Label("", skin);
        errLabel.setColor(Color.RED);

        Dialog dialog = new Dialog("", skin) {
            @Override
            protected void result(Object object) {
                if ((Boolean) object) {
                    String newPassword = newPasswordField.getText();
                    String confirmPassword = confirmPasswordField.getText();

                    if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
                        errLabel.setText("Please fill all the fields");
                        shakeDialog(this);
                        cancel();
                        return;
                    }
                    if (!newPassword.equals(confirmPassword)) {
                        errLabel.setText("Passwords do not match");
                        shakeDialog(this);
                        cancel();
                        return;
                    }
                    if (!Player.isValidPassword(newPassword, errLabel)) {
                        errLabel.setText("Invalid password");
                        shakeDialog(this);
                        cancel();
                        return;
                    }

                    currentPlayer.setPassword(newPassword);
                    currentPlayer.save();
                    view.showSuccessMessage("Password changed successfully");
                }
                addAction(Actions.sequence(Actions.fadeOut(0.3f), Actions.run(this::remove)));
            }
        };

        Table content = dialog.getContentTable();

        content.add(new Label("New Password:", skin)).padTop(10).row();
        content.add(newPasswordField).width(400).padBottom(10).row();
        content.add(confirmPasswordField).width(400).padTop(10).row();
        content.add(errLabel).width(400).padBottom(10).row();
        content.add(errLabel).padTop(10).row();

        dialog.button("Ok", true).button("Cancel", false);
        showAnimatedDialog(dialog);
    }

    public void handleDeleteAccount() {
        currentPlayer.delete();
        Main.getMain().setScreen(new RegisterMenuView(new RegisterMenuController(),skin));
    }

    public void handleBack() {
        Main.getMain().setScreen(new MainMenuView(new MainMenuController(),skin, currentPlayer));
    }

    public void handleChangeAvatar() {
        Dialog mainDialog = new Dialog("", skin);

        TextButton galleryButton = new TextButton("Choose from Gallery", skin);
        TextButton uploadButton = new TextButton("Upload from File", skin);
        Label dropLabel = new Label("... or Drag & Drop an image here", skin);

        galleryButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                mainDialog.hide();
                showGalleryDialog();
            }
        });

        uploadButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                mainDialog.hide();
                showFileChooser();
            }
        });

        Table content = mainDialog.getContentTable();
        content.pad(20);
        content.add(galleryButton).width(400).pad(10).row();
        content.add(uploadButton).width(400).pad(10).row();
        content.add(dropLabel).pad(20).row();

        mainDialog.button("Cancel");
        mainDialog.show(view.getStage());
    }

    private void showGalleryDialog() {
        Dialog galleryDialog = new Dialog("Choose from Gallery", skin);
        Table table = galleryDialog.getContentTable();
        table.pad(20);

        Avatar[] defaultAvatars = {Avatar.Dasher, Avatar.Diamond, Avatar.Lilith, Avatar.Scarlett, Avatar.Shana};
        for (Avatar avatar : defaultAvatars) {
            Image avatarImage = new Image(avatar.getPortrait());
            Button avatarButton = new Button(avatarImage, skin);
            avatarButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    updatePlayerAvatar(avatar, null);
                    galleryDialog.hide();
                }
            });
            table.add(avatarButton).size(96, 96).pad(10);
        }
        galleryDialog.button("Back").addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                handleChangeAvatar(); // بازگشت به دیالوگ اصلی
            }
        });
        galleryDialog.show(view.getStage());
    }

    private void showFileChooser() {
        new Thread(() -> {
            FileDialog dialog = new FileDialog((Frame) null, "Select Avatar Image");
            dialog.setMode(FileDialog.LOAD);
            dialog.setVisible(true);

            String file = dialog.getFile();
            String dir = dialog.getDirectory();

            if (file != null && dir != null) {
                final String fullPath = dir + file;

                Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        processNewAvatarFile(fullPath);
                    }
                });
            }
        }).start();
    }

    public void processNewAvatarFile(String filePath) {
        try {
            File inputFile = new File(filePath);
            BufferedImage originalImage = ImageIO.read(inputFile);

            if (originalImage == null) {
                throw new IOException("Unsupported image format.");
            }

            int targetSize = 128;
            BufferedImage resizedImage = new BufferedImage(targetSize, targetSize, BufferedImage.TYPE_INT_ARGB);
            resizedImage.createGraphics().drawImage(originalImage, 0, 0, targetSize, targetSize, null);

            FileHandle localDestFile = Gdx.files.local(currentPlayer.getCustomAvatarPath()); // از Player بگیرید

            ImageIO.write(resizedImage, "png", localDestFile.file());

            Gdx.app.log("AvatarChange", "New avatar resized and saved to: " + localDestFile.path());

            updatePlayerAvatar(Avatar.Pc, null);

        } catch (Exception e) {
            Gdx.app.error("AvatarChange", "Failed to process new avatar file.", e);
            if (view != null) {
                view.showErrorMessage("Failed to load image. Please use a valid image file.");
            }
        }
    }

    private void updatePlayerAvatar(Avatar newAvatar, String customFileName) {
        currentPlayer.setAvatar(newAvatar);
        currentPlayer.save();
        view.showSuccessMessage("Avatar changed successfully!");
        view.updateAvatarDisplay();
    }

    private void showAnimatedDialog(Dialog dialog) {
        dialog.setModal(true);
        dialog.getColor().a = 0f;
        dialog.setTransform(true);
        dialog.setScale(0f);
        dialog.show(view.getStage());
        dialog.addAction(Actions.sequence(
            Actions.parallel(
                Actions.fadeIn(0.3f),
                Actions.scaleTo(1f, 1f, 0.3f, Interpolation.swingOut)
            )
        ));
    }

    private void shakeDialog(Dialog dialog) {
        dialog.clearActions();
        dialog.addAction(Actions.sequence(
            Actions.moveBy(-10, 0, 0.05f, Interpolation.sine),
            Actions.moveBy(20, 0, 0.05f, Interpolation.sine),
            Actions.moveBy(-10, 0, 0.05f, Interpolation.sine)
        ));
    }
}
