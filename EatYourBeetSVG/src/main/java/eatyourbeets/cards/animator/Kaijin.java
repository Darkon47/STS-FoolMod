package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.interfaces.OnAddedToDeckSubscriber;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.powers.animator.KaijinPower;
import eatyourbeets.utilities.RandomizedList;

public class Kaijin extends AnimatorCard implements OnAddedToDeckSubscriber
{
    public static final String ID = Register(Kaijin.class.getSimpleName(), EYBCardBadge.Special);

    public Kaijin()
    {
        super(ID, 1, CardType.POWER, CardRarity.UNCOMMON, CardTarget.SELF);

        Initialize(0,0, 1);

        SetSynergy(Synergies.TenSura);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActionsHelper.ApplyPower(p, p, new KaijinPower(p, this.magicNumber), this.magicNumber);
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeBaseCost(0);
        }
    }

    @Override
    public void OnAddedToDeck()
    {
        RandomizedList<AbstractCard> upgradableCards = new RandomizedList<>();
        for (AbstractCard c : AbstractDungeon.player.masterDeck.group)
        {
            if (CardRarity.BASIC.equals(c.rarity) && c.canUpgrade())
            {
                upgradableCards.Add(c);
            }
        }

        if (upgradableCards.Count() > 0)
        {
            AbstractCard card1 = upgradableCards.Retrieve(AbstractDungeon.cardRandomRng);
            card1.upgrade();
            AbstractDungeon.player.bottledCardUpgradeCheck(card1);
            AbstractDungeon.topLevelEffects.add(new ShowCardBrieflyEffect(card1.makeStatEquivalentCopy(), (float) Settings.WIDTH / 2.0F + AbstractCard.IMG_WIDTH / 2.0F + 20.0F * Settings.scale, (float) Settings.HEIGHT / 2.0F));
            AbstractDungeon.topLevelEffects.add(new UpgradeShineEffect((float) Settings.WIDTH / 2.0F, (float) Settings.HEIGHT / 2.0F));
        }
    }
}