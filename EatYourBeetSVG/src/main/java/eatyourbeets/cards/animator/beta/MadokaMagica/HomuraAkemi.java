package eatyourbeets.cards.animator.beta.MadokaMagica;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.watcher.PressEndTurnButtonAction;
import com.megacrit.cardcrawl.actions.watcher.SkipEnemiesTurnAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.WhirlwindEffect;
import eatyourbeets.actions.cardManipulation.ScryWhichActuallyTriggerDiscard;
import eatyourbeets.cards.base.*;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class HomuraAkemi extends AnimatorCard
{
    public static final EYBCardData DATA = Register(HomuraAkemi.class).SetSkill(3, CardRarity.UNCOMMON, EYBCardTarget.None);

    public HomuraAkemi()
    {
        super(DATA);

        Initialize(0, 0);
        SetExhaust(true);

        SetSynergy(Synergies.MadokaMagica);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        GameActions.Bottom.VFX(new WhirlwindEffect(new Color(0.2F, 0.0F, 0.2F, 1.0F),true));

        GameActions.Bottom.Add(new SkipEnemiesTurnAction());

        for (int i = 0; i < 3; i++ )
        {
            GameActions.Bottom.MakeCardInDiscardPile(GameUtilities.GetRandomCurse());
        }

        if (upgraded)
        {
            GameActions.Bottom.Add(new ScryWhichActuallyTriggerDiscard(4));
        }

        GameActions.Bottom.Add(new PressEndTurnButtonAction());
    }
}