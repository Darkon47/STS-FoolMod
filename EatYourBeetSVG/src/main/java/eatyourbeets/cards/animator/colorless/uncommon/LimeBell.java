package eatyourbeets.cards.animator.colorless.uncommon;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;

public class LimeBell extends AnimatorCard
{
    public static final EYBCardData DATA = Register(LimeBell.class).SetSkill(2, CardRarity.UNCOMMON, EYBCardTarget.None).SetColor(CardColor.COLORLESS);

    public LimeBell()
    {
        super(DATA);

        Initialize(0, 8, 2);
        SetUpgrade(0, 4, 0);

        SetExhaust(true);
        SetSynergy(Synergies.AccelWorld);
        SetAffinity_Light(2);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.Reload(name, cards -> GameActions.Bottom.GainTemporaryHP(cards.size() * magicNumber));
    }
}