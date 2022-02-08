package pinacolada.ui.common;

import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.RestRoom;
import com.megacrit.cardcrawl.ui.campfire.AbstractCampfireOption;
import pinacolada.cards.fool.colorless.Kirby;
import pinacolada.resources.PGR;
import pinacolada.utilities.PCLGameEffects;

public class KirbyCampfireOption extends AbstractCampfireOption
{
    private final Kirby kirby;
    private RestRoom room;

    public static boolean CanAddOption()
    {
        return AbstractDungeon.player.masterDeck.findCardById(Kirby.DATA.ID) != null;
    }

    public KirbyCampfireOption()
    {
        kirby = (Kirby) AbstractDungeon.player.masterDeck.findCardById(Kirby.DATA.ID);
        description = PGR.PCL.Strings.CardMods.KirbyDescription;
        label = PGR.PCL.Strings.CardMods.Kirby;
        img =  PGR.PCL.Images.CampfireOption_Kirby.Texture();
    }

    @Override
    public void useOption()
    {
        if (!usable || kirby == null)
        {
            PCLGameEffects.Queue.Callback(new WaitAction(0.1f), ((RestRoom) AbstractDungeon.getCurrRoom()).campfireUI::reopen);
            return;
        }

        room = (RestRoom) AbstractDungeon.getCurrRoom();
        room.campfireUI.somethingSelected = true;

        PCLGameEffects.Queue.Callback(kirby::RemoveInheritedCards).AddCallback(() -> {
            room.campfireUI.reopen();
        });
    }
}