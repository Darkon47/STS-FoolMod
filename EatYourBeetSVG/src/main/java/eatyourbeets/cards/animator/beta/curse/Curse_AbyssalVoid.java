package eatyourbeets.cards.animator.beta.curse;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard_Curse;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Curse_AbyssalVoid extends AnimatorCard_Curse
{
    public static final EYBCardData DATA = Register(Curse_AbyssalVoid.class).SetCurse(-1, EYBCardTarget.None, true);

    public Curse_AbyssalVoid()
    {
        super(DATA, false);

        Initialize(0, 0, 6, 3);
        SetUpgrade(0, 0, 0, 0);
        SetAffinity_Dark(1);

        SetAutoplay(true);
        SetPurge(true);
        SetEthereal(true);
    }


    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        int stacks = GameUtilities.UseXCostEnergy(this);
        GameActions.Bottom.GainDesecration(magicNumber * stacks);
        GameActions.Bottom.DealDamageAtEndOfTurn(player, player,secondaryValue * stacks, AttackEffects.DARKNESS);
    }
}