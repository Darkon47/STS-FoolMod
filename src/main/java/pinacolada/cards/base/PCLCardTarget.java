package pinacolada.cards.base;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.actions.special.SelectCreature;
import eatyourbeets.utilities.TargetHelper;
import pinacolada.resources.PGR;

public enum PCLCardTarget implements Comparable<PCLCardTarget>
{
    // The ordering of this enum determines which targeting system takes priority
    None(AbstractCard.CardTarget.NONE, SelectCreature.Targeting.None, TargetHelper.Mode.Source, null),
    AoE(AbstractCard.CardTarget.ALL_ENEMY, SelectCreature.Targeting.AoE, TargetHelper.Mode.Enemies, "AoE"),
    All(AbstractCard.CardTarget.ALL, SelectCreature.Targeting.AoE, TargetHelper.Mode.AllCharacters, null),
    Self(AbstractCard.CardTarget.SELF, SelectCreature.Targeting.Player, TargetHelper.Mode.Player, null),
    Normal(AbstractCard.CardTarget.ENEMY, SelectCreature.Targeting.Enemy, TargetHelper.Mode.Normal, null),
    Any(AbstractCard.CardTarget.SELF_AND_ENEMY, SelectCreature.Targeting.Any, TargetHelper.Mode.Normal, null),
    Random(AbstractCard.CardTarget.ALL_ENEMY, SelectCreature.Targeting.Random, TargetHelper.Mode.RandomEnemy, "???"),
    Ally(AbstractCard.CardTarget.ENEMY, SelectCreature.Targeting.PlayerMinion, TargetHelper.Mode.PlayerMinions, null);

    public final AbstractCard.CardTarget cardTarget;
    public final SelectCreature.Targeting selectMode;
    public final TargetHelper.Mode targetMode;
    public final String tag;

    private PCLCardTarget(AbstractCard.CardTarget cardTarget, SelectCreature.Targeting selectMode, TargetHelper.Mode targetMode, String tag) {
        this.cardTarget = cardTarget;
        this.selectMode = selectMode;
        this.targetMode = targetMode;
        this.tag = tag;
    }

    public final TargetHelper GetTarget(AbstractCreature m) {
        switch (targetMode) {
            case AllCharacters:
                return TargetHelper.AllCharacters();
            case Enemies:
                return TargetHelper.Enemies();
            case Normal:
                return TargetHelper.Normal(m);
            case Player:
                return TargetHelper.Player();
            case PlayerMinions:
                return TargetHelper.PlayerMinions();
            case Random:
                return TargetHelper.RandomCharacter();
            case RandomEnemy:
                return TargetHelper.RandomEnemy();
            case Source:
                return TargetHelper.Source();
        }
        throw new RuntimeException("Target for CardTarget should not be null.");
    }

    // These strings cannot be put in as an enum variable because cards are initialized before these strings are
    public final String GetTitle() {
        switch (this) {
            case None:
                return PGR.PCL.Strings.CardTarget.None;
            case AoE:
                return PGR.PCL.Strings.CardTarget.AoE;
            case All:
                return PGR.PCL.Strings.CardTarget.All;
            case Self:
                return PGR.PCL.Strings.CardTarget.Self;
            case Normal:
                return PGR.PCL.Strings.CardTarget.Normal;
            case Any:
                return PGR.PCL.Strings.CardTarget.Any;
            case Random:
                return PGR.PCL.Strings.CardTarget.Random;
            case Ally:
                return PGR.PCL.Strings.CardTarget.Ally;
        }
        return "";
    }
}
